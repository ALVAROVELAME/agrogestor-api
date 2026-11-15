"""Consulta dependências Maven resolvidas na OSV. Usa apenas a biblioteca padrão Python."""
import argparse
import datetime
import json
from pathlib import Path
import sys
import urllib.request


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--tree", default="target/dependency-tree.json")
    parser.add_argument("--output", default="target/security-audit.json")
    args = parser.parse_args()
    dependencies = set()

    def walk(node):
        for child in node.get("children", []):
            dependencies.add((child["groupId"] + ":" + child["artifactId"], child["version"]))
            walk(child)

    walk(json.loads(Path(args.tree).read_text(encoding="utf-8")))
    if not dependencies:
        raise ValueError("Árvore de dependências vazia; gere novamente com dependency:tree")
    queries = [{"package": {"name": name, "ecosystem": "Maven"}, "version": version}
               for name, version in sorted(dependencies)]
    findings = []
    for start in range(0, len(queries), 100):
        batch = queries[start:start + 100]
        request = urllib.request.Request(
            "https://api.osv.dev/v1/querybatch",
            data=json.dumps({"queries": batch}).encode(),
            headers={"Content-Type": "application/json"},
        )
        with urllib.request.urlopen(request, timeout=60) as response:
            results = json.load(response)["results"]
        if len(results) != len(batch):
            raise RuntimeError("Resposta incompleta da OSV")
        for query, result in zip(batch, results):
            if result.get("vulns"):
                findings.append({"name": query["package"]["name"], "version": query["version"],
                                 "vulnerabilities": result["vulns"]})
    report = {"source": "https://osv.dev", "checked_at": datetime.datetime.now(datetime.timezone.utc).isoformat(),
              "packages": len(dependencies), "findings": findings}
    Path(args.output).write_text(json.dumps(report, indent=2), encoding="utf-8")
    print(f"Pacotes consultados: {len(dependencies)}; pacotes com avisos: {len(findings)}")
    for finding in findings:
        print(finding["name"], finding["version"], ", ".join(v["id"] for v in finding["vulnerabilities"]))
    return 1 if findings else 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except (OSError, ValueError, KeyError, RuntimeError) as error:
        print(f"Não foi possível concluir a auditoria: {error}", file=sys.stderr)
        sys.exit(2)
