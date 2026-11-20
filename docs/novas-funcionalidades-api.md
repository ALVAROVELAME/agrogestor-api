# Novas funcionalidades da API

As rotas abaixo exigem `Authorization: Bearer <token>`. Os registros são associados ao usuário autenticado. Datas seguem `AAAA-MM-DD`. As listagens usam `pagina` (começa em 0) e `tamanho` (1 a 100, padrão 20), retornando uma página com `content` e metadados.

| Recurso | Métodos e rotas |
| --- | --- |
| Ficha | `GET /api/animais/{id}`, `PATCH /api/animais/{id}/ficha` |
| Produção | `GET, POST /api/producoes`, `PUT, DELETE /api/producoes/{id}` |
| Tarefas | `GET, POST /api/tarefas`, `PATCH, DELETE /api/tarefas/{id}` |
| Eventos | `GET, POST /api/eventos`, `PUT, DELETE /api/eventos/{id}` |
| Financeiro | `GET, POST /api/financeiro/lancamentos`, `PUT, DELETE /api/financeiro/lancamentos/{id}` |
| Relatórios | `GET /api/relatorios/producao`, `GET /api/relatorios/financeiro`, `GET /api/relatorios/eventos`, `GET /api/relatorios/exportacao` |
| Indicadores | `GET /api/indicadores` |

São 23 operações novas. A ficha aceita `dataNascimento`, `raca`, `origem` e `observacoes`; campos omitidos ou nulos não são alterados, e textos podem ser limpos com uma string vazia.

Exemplos de corpos para criação:

```json
{"animalId": 1, "data": "2026-01-20", "turno": "MANHA", "litros": 18.50}
```
```json
{"titulo": "Revisar vacinação", "data": "2026-01-25", "animalId": 1}
```
```json
{"animalId": 1, "data": "2026-01-20", "tipo": "VACINACAO", "descricao": "Vacinação anual"}
```
```json
{"data": "2026-01-20", "tipo": "DESPESA", "descricao": "Ração", "valor": 120.50}
```

Os turnos são `MANHA`, `TARDE` e `NOITE`; eventos aceitam `CONSULTA`, `VACINACAO`, `TRATAMENTO`, `INSEMINACAO`, `PARTO` e `OUTRO`; o financeiro aceita `RECEITA` e `DESPESA`. `animalId` é opcional em tarefas e obrigatório em eventos e produção. A produção não aceita data futura nem duplicação de animal, data e turno. Os valores financeiros são positivos; o tipo determina se são receita ou despesa.

Nas respostas de tarefas, eventos e financeiro, `descricao` contém o título ou descrição, e `subtipo` contém o tipo do evento ou lançamento. A atualização parcial de tarefa aceita `titulo`, `data` e `concluida`.

As listagens aceitam `inicio` e `fim`; produção aceita também `animalId` e `turno`, tarefas aceitam `animalId` e `concluida`, eventos aceitam `animalId` e `tipo`, e financeiro aceita `tipo`. Os relatórios exigem `inicio` e `fim`, com período de até 366 dias; o de produção aceita `animalId`. Indicadores exigem ainda `referencia`, usada para determinar tarefas atrasadas: os contadores de tarefas abrangem todas as tarefas pendentes, enquanto produção e financeiro respeitam o período. A exportação aceita `tipo=PRODUCAO`, `FINANCEIRO` ou `EVENTOS` e gera CSV agregado.

A exclusão de um animal também exclui seus registros associados de produção, eventos e tarefas. A exclusão do usuário exclui seus registros. O projeto utiliza `spring.jpa.hibernate.ddl-auto=update`; valide a atualização de esquema em ambiente de teste antes de aplicá-la em produção. O frontend atualizado utiliza essas rotas; os antigos dados de demonstração em memória não são migrados automaticamente.
