### Gestão de Editais e Seleção

```mermaid
classDiagram
class Edital{
		-String: id
		-String: titulo
		-String: data
		-String: descricao
	    -String: calendario
	    -String: preRequisitos
	    -String: formInscricao
	    +adicionar(edital Edital) void
	    +remover(String titulo) void
	    +listarEditais() List~Edital~
	    +listarInscritos() List~Participante~
	}
```