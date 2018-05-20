# Documentație unit testing

### Metoda *parse()* din clasa RequestParser

Input:
```json
{
    "operation": "select",
    "from": "students",
    "cond1": {
        "key": "id",
        "operator": "=",
        "value": "1"
        },
    "cond2": {
        "key": "first_name",
        "operator": "=",
        "value": "Mircea"
        },
    "logical_condition": "or"
}
```

Output: 
-	Operație: variabilă de tipul OPERATION.SELECT
-	Tabel: String ”students”
-	Condiția 1: 
    -	key = id
    -	operator = OPERATOR.E
    -	value = 1
-	Condiția 2:
    -	key = first_name
    -	operator = OPERATOR.E
    -	value = ”Mircea”
-	Condiția logică: LOGICAL_CONDITION.OR


### Metoda *handleOperation()* din clasa RequestParser

| INPUT | OUTPUT | 
| :-: | :-: | 
| select | OPERATION.SELECT |
| create | OPERATION.CREATE |
| insert | OPERATION.INSERT | 
| insert_array | OPERATION.INSERT_ARRAY | 
| delete | OPERATION.DELETE | 
| update | OPERATION.UPDATE | 
| drop | OPERATION.DROP | 
| get | OPERATION.INVALID_OPERATION | 


### Metoda *handleLogicalCondition()* din clasa RequestParser

| INPUT | OUTPUT | 
| :-: | :-: | 
| and | CONDITION_LOGIC.AND |
| or | CONDITION_LOGIC.OR |
| maybe | CONDITION_LOGIC.INVALID_CONDITION |


### Metoda *handleOperator()* din clasa RequestParser

| INPUT | OUTPUT | 
| :-: | :-: | 
| = | OPERATOR.E |
| > | OPERATOR.G |
| < | OPERATOR.L | 
| >= | OPERATOR.GE | 
| <= | OPERATOR.LE | 
| <> | INVALID_OPERATOR | 


## Teste pentru clasa utils

	Vom considera același input pentru toate testele cu select, iar in funcție de metoda apelată va fi produs un output diferit.
 
Input:
```json
{
    "operation": "select",
    "from": "students",
    "cond1": {
        "key": "id",
        "operator": "=",
        "value": "1"
        },
    "cond2": {
        "key": "first_name",
        "operator": "=",
        "value": "Mircea"
        },
    "logical_condition": "or"
}
```

Metoda **getFilteredObjects()** – returnează un JSONArray cu obiectele din baza de date care satisfac condițiile din input. În acest caz toți studenții care au id-ul *1* sau first_name *Mircea*.

Output:
```json
[{
    "medie": 7.5,
    "last_name": "Mihai",
    "nota_examen": 7,
    "id": 14,
    "medie_bac": 8,
    "first_name": "Mircea"
}, {
    "medie": 6,
    "last_name": "Mihai",
    "nota_examen": 7,
    "id": 16,
    "medie_bac": 8,
    "first_name": "Mircea"
}]
```

Metoda **getDifferenceFilteredObjects()** returneză opusul funcției de mai sus adică userii care nu au id-ul *1* și numele diferit de *Mircea*.

Output:
```json
[{
    "medie": 9.5,
    "last_name": "Port",
    "nota_examen": 9,
    "id": 3,
    "medie_bac": 10,
    "first_name": "Ionel"
}, {
    "medie": 8,
    "last_name": "ionel",
    "nota_examen": 8,
    "id": 5,
    "medie_bac": 8,
    "first_name": "popescu"
}, {
    "medie": 3.495,
    "last_name": "test",
    "nota_examen": 0.01,
    "id": 6,
    "medie_bac": 6.98,
    "first_name": "test nou"
}, {
    "medie": 7.49,
    "last_name": "test now",
    "nota_examen": 8,
    "id": 8,
    "medie_bac": 6.98,
    "first_name": "test"
}, {
    "medie": 7.99,
    "last_name": "test now 2",
    "nota_examen": 9,
    "id": 9,
    "medie_bac": 6.98,
    "first_name": "test"
}, {
    "medie": 5,
    "last_name": "Ion",
    "nota_examen": 5,
    "id": 10,
    "medie_bac": 5,
    "first_name": "Dorian"
}, {
    "medie": 7,
    "last_name": "ion",
    "nota_examen": 5,
    "id": 11,
    "medie_bac": 9,
    "first_name": "ion"
}, {
    "medie": 8.825,
    "last_name": "Grigore",
    "nota_examen": 9.2,
    "id": 12,
    "medie_bac": 8.45,
    "first_name": "Vasile"
}, {
    "medie": 8.825,
    "last_name": "Grigore",
    "nota_examen": 9.2,
    "id": 13,
    "medie_bac": 8.45,
    "first_name": "Vasile"
}, {
    "medie": 8.5,
    "last_name": "Grigore",
    "nota_examen": 9,
    "id": 15,
    "medie_bac": 8,
    "first_name": "Vasile"
}, {
    "medie": 8.5,
    "last_name": "Grigore",
    "nota_examen": 9,
    "id": 17,
    "medie_bac": 8,
    "first_name": "Vasile"
}, {
    "medie": 10,
    "last_name": "Cusmuliuc",
    "nota_examen": 10,
    "id": 18,
    "medie_bac": 10,
    "first_name": "Ciprian-Gabriel"
}]
```

Metoda *evaluateLogicalCondition()* returnează o valoare Boolean și verifica daca sunt validate condițiile în cazul în care se știe valoarea primei condiții.

Input: 
- rezultat condiție 1 = true
- CONDITIE = and
Output: true

Input:
- rezultat condiție 1 = true
- CONDITIE = OR
Output: true

Metoda *evaluateMissingValues()* verifică dacă json-ul primit în request pentru operașia de update, conține cheia values.

Metoda *getUpdatedObjects()* returnează un JSONArray cu obiectele din baza de date, unde cele care satisfac condițiile din input, vor avea modificate valorile date în values. În acest caz toți studenții care au id-ul >= *15* sau first_name *Mircea* vor avea media 8.


Input:
```json
{
	"operation": "update",
	"table": "students",
	"values": {
		"medie": 8
	},
	"cond1": {
		"key": "id",
		"operator": ">=",
		"value": 15
		},
	"cond2": {
		"key": "first_name",
		"operator": "=",
		"value": "Mircea"
		},
	"logical_condition": "OR"	
}
```

Output:
```json
[
...
 {
    "medie": 8,
    "last_name": "Mihai",
    "nota_examen": 7,
    "id": 16,
    "medie_bac": 8,
    "first_name": "Mircea"
}
...
]
```

Metoda *getUpdatedObjectsWrongFormat()* returnează codul de eroare -30, deoarece pentru cheia medie din următorul input, s-a dat un string, în loc de număr.

Input:
```json
{
	"operation": "update",
	"table": "students",
	"values": {
		"medie": "8"
	},
	"cond1": {
		"key": "id",
		"operator": ">=",
		"value": 15
		},
	"cond2": {
		"key": "first_name",
		"operator": "=",
		"value": "Mircea"
		},
	"logical_condition": "OR"	
}
```

## Clasa ServletController valideaza condițiile pentru fiecare request și întoarce un rezultat.


Metoda **getSelectData()** verifică rezultatele și întoarce statusul corespunzător pentru request.

Input:
```json
[{
    "response": 1
}]
```
Output: 200

Input:
```json
[{
    "response": -13
}]
```
Output: 400


Metoda **checkBasicData()** verifică validitatea unui request.

***Request OK***

Input:
```json
{
    "operation": "select",
    "from": "students",
    "cond1": {
        "key": "id",
        "operator": "<=",
        "value": 10
        }
}
```

Output:
```json
[{
    "response": 1
}]
```

***Request fără tabel***

Input:
```json
{
    "operation": "select",
    "from": "",
    "cond1": {
        "key": "id",
        "operator": "<=",
        "value": 10
        }
}
```

Output:
```json
[{
    "response": -11
}]
```

***Request fără operație***

Input:
```json
{
    "operation": "",
    "from": "students",
    "cond1": {
        "key": "id",
        "operator": "<=",
        "value": 10
        }
}
```

Output:
```json
[{
    "response": -11
}]
```

***Request fără valoare în condiție***

Input:
```json
{
    "operation": "select",
    "from": "students",
    "cond1": {
        "key": "id",
        "operator": "<=",
        "value":
        }
}
```

Output:
```json
[{
    "response": -11
}]
```

***Request cu un operator logic invalid**

Input: 
```json
{
    "operation": "select",
    "from": "students",
    "cond1": {
        "key": "id",
        "operator": "<:",
        "value":
        }
}
```

Output:
```json
[{
    "response": -13
}]
```

***Request fără cheie în condiție***

Input:
```json
{
    "operation": "select",
    "from": "students",
    "cond1": {
        "key": "",
        "operator": "<=",
        "value": 10
        }
}
```

Output: 
```json
[{
    "response": -14
}]
```
