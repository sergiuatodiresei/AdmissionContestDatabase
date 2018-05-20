# Documentație unit testing

Metoda **parse()** din clasa RequestParser

```json
INPUT:
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
-	Operație: variabila de tipul OPERATION.SELECT
-	Tabel: String ”students”
-	Condiția 1: 
    -	key = id
    -	operator = OPERATOR.E
    -	value = 1
-	Condiția 2:
o	Key = first_name
o	Operator = OPERATOR.E
o	Value = ”Mircea”
-	Condiția logică: LOGICAL_CONDITION.OR
