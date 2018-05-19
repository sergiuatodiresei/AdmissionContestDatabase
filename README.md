# AdmissionContestDatabase

**URL API**

## http://admissioncontest.us-east-2.elasticbeanstalk.com/AdmissionContestServlet

URL fisier *students.json*:

###  https://s3.amazonaws.com/admission-contest/database/students.json

URL fisier *struct_students.json*:

###  https://s3.amazonaws.com/admission-contest/database/struct_students.json

URL fisier *criterii_insert_students.json*:

###  https://s3.amazonaws.com/admission-contest/database/criterii_insert_students.json



### Posibile erori generale:
- In caz ca nu se citeste bine json-ul primit, raspunsul va fi: **{ "response": -1}**
- In caz ca nu se citeste bine un fisier din s3 (amazon) sau tabelul(fisierul) nu exista, raspunsul va fi: **{ "response": -10}**
- In caz ca nu este primit in json numele tabelui si/sau operatia ceruta, raspunsul va fi : **{ "response": -11}**
- In caz ca se trimite o operatie gresita (*operation*), raspunsul va fi: **{ "response": -12}**
<br />In cadrul unei conditii:
- In caz ca se trimite un operator gresit (*operator*) sau nu se trimite deloc, raspunsul va fi: **{ "response": -13}**
- In caz ca se trimite o cheie gresita (*key*) sau nu se trimite deloc, raspunsul va fi: **{ "response": -14}**
- In caz ca se trimite o valoare de comparatie gresita (*value*) sau nu se trimite deloc, raspunsul va fi: **{ "response": -15}**
- In caz ca se trimit 2 conditii, iar *"logical_condition"* nu este trimis sau este trimis ceva invalid, raspunsul va fi: **{ "response": -23}**
- In caz ca nu se citesc bine conditiile: **{ "response": -24}**


### Cheia "operation" poate avea urmatoarele valori:
- **select**
- **insert**
- **update**
- **delete**
- **create**
- **drop**

## Pentru a specifica tabelul pot fi trimise urmatoarele valori:
- **from**
- **into**
- **table**

### Cheia "operator" poate avea urmatoarele valori:
- **=**
- **<**
- **>**
- **<=** 
- **>=**


### Cheia "logical_condition" reprezinta conditia dintre *cond1* si *cond2* si poate avea urmatoarele valori:
- **AND**
- **OR**


# SELECT

Se face request prin POST cu un json, cu **maxim** 2 conditii ("cond1", "cond2").
Se trimite **"operation": "select"** si **"from": "nume_tabel"**

**Raspunsul va fi un array de json-uri.**

### Cheia "key" reprezinta "coloana" din tabel si poate avea urmatoarele valori pentru tabelul *students*:
- **id**
- **first_name**
- **last_name**
- **medie_bac**
- **nota_examen**
- **medie**


## Exemple de teste:

### Ex. 1: 

```json
{
	"operation": "select",
	"from": "students"
}
```

- Traducere in sql: *SELECT * FROM students*


### Ex. 2: 

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

- Traducere in sql: *SELECT * FROM students WHERE id <= 10*

### Ex. 3: 

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

- Traducere in sql: *SELECT * FROM students WHERE id = 1 OR first_name = 'Mircea'*


# INSERT

Se face request prin POST cu un json, cu **"operation": "insert"** si **"into": "nume_tabel"**
Se trimite perechea cheia **"values"** si un json ce contine "coloanele" (Vezi exemplu)
ID-ul nu se trimite (el se autoincrementeaza). Nu e necesar sa fie trimise toate coloanele. Coloanele lipsa vor adaugate automat cu valoarea "".
Pentru tabelul students, media nu trebuie trimisa, se calculeaza automat pe baza datelor din tabelul **criterii_insert_students**, dar **"medie_bac"** si **"nota_examen"** trebuie.

**Daca insert-ul s-a efectuat cu succes, raspunsul va fi {"response": 1}**

## Exemple de teste:

### Ex. 1: 

```json
{
	"operation": "insert",
	"into": "students",
	"values": {
		"first_name": "Prenume_student",
		"last_name": "Nume_student"
		"medie_bac": 10,
		"nota_examen": 9
	}
}
```

- Traducere in sql: *INSERT INTO students VALUES("Prenume_student", "Nume_student", 10, 9)*


### Ex. 2: 

```json
{
	"operation": "insert",
	"into": "students",
	"values": {
		"first_name": "Prenume_student",
		"medie_bac": 10,
		"nota_examen": 9
	}
}
```

- Traducere in sql: *INSERT INTO students VALUES("Prenume_student", "", 10, 9)*

## Posibile erori:

- In caz ca nu se trimite values, raspunsul va fi: **{ "response": -16}**
- In caz ca nu s-a putut citi tabelul "criterii_insert_students" raspunsul va fi: **{ "response": -17}**
- In caz ca nu se trimite medie_bac, raspunsul va fi: **{ "response": -18}**
- In caz ca nu se trimite nota_examen, raspunsul va fi: **{ "response": -19}**
- In caz ca pentru medie_bac se trimit alte valori din intervalul [5,10], raspunsul va fi: **{ "response": -20}**
- In caz ca pentru nota_examen se trimit alte valori din intervalul [0,10], raspunsul va fi: **{ "response": -21}**
- In caz ca se trimite o valoarea de tip gresit (in loc de number, se trimite text si invers), raspunsul va fi: **{ "response": -30}**
De exemplu: "medie_bac": "10" sau "first_name": 0,
- Alte erori - **{ "response": -22}**


# INSERT_ARRAY

## La fel ca INSERT, doar ca se trimite un array la values

Se face request prin POST cu un json, cu **"operation": "insert_array"** si **"into": "nume_tabel"**
Se trimite perechea **"values_array"** si un json array.

**Daca insert-ul s-a efectuat cu succes, raspunsul va fi {"response": 1}**

## Exemplu de test:

```json
{
	"operation": "insert_array",
	"into": "students",
	"values_array": [{
		"first_name": "Gigi",
		"last_name": "Buhnici",
		"medie_bac": 10,
		"nota_examen": 9
	},
	{
		"first_name": "Mircea",
		"last_name": "Iosif",
		"medie_bac": 10,
		"nota_examen": 10
	}]
}
```


# UPDATE

--Asemanator cu SELECT, are in plus campul json-ul *values*

Se face request prin POST cu un json, cu **"operation": "update"** si **"table": "nume_tabel"**
Se trimite perechea cheia **"values"** si un json ce contine "coloanele" (Vezi exemplu)

**Daca update-ul s-a efectuat cu succes, raspunsul va fi {"response": 1}**

## Exemple de teste:

### Ex. 1: 

```json
{
	"operation": "update",
	"table": "students",
	"values": {
		"last_name": "Port"
	}
		
}
```

- Traducere in sql: *UPDATE students SET last_name = "Port"*
- Se modifica toate randurile din tabel


### Ex. 2: 

```json
{
	"operation": "update",
	"table": "students",
	"values": {
		"last_name": "Port"
	},
	"cond1": {
		"key": "id",
		"operator": "=",
		"value": 4
		}
		
}
```

- Traducere in sql: *UPDATE students SET last_name = "Port" WHERE id = 4*

### Ex. 3: 

```json
{
	"operation": "update",
	"table": "students",
	"values": {
		"last_name": "Port"
	},
	"cond1": {
		"key": "id",
		"operator": "=",
		"value": 4
		},
	"cond2": {
		"key": "id",
		"operator": "=",
		"value": 5
		},
	"logical_condition": "OR"
		
}
```

- Traducere in sql: *UPDATE students SET last_name = "Port" WHERE id = 4 or id = 5*

## Posibile erori:

- In caz ca nu se trimite values, raspunsul va fi: **{ "response": -16}**
- In caz ca se trimite o valoarea de tip gresit (in loc de number, se trimite text si invers), raspunsul va fi: **{ "response": -30}**
De exemplu: "medie_bac": "10" sau "last_name": 0,

# DELETE

**-- Foarte asemanator cu SELECT**

Se face request prin POST cu un json, cu **"operation": "delete"** si **"from": "nume_tabel"** si **maxim** 2 conditii ("cond1", "cond2").

**Daca delete-ul s-a efectuat cu succes, raspunsul va fi {"response": 1}**

## Exemple de teste:

### Ex. 1: 

```json
{
	"operation": "delete",
	"from": "students",
}
```

- Traducere in sql: *DELETE from students"*
**- Se sterg toate randurile din tabel!**


### Ex. 2: 

```json
{
	"operation": "delete",
	"from": "students",
	"cond1": {
		"key": "id",
		"operator": "=",
		"value": 4
		},
	"cond2": {
		"key": "first_name",
		"operator": "=",
		"value": "Paula"
		},
	"logical_condition": "or"
}
```

- Traducere in sql: *DELETE from students WHERE last_name = "Port" WHERE id = 4 or id = 5*


# CREATE

**-- Asemanator cu INSERT**

Se face request prin POST cu un json, cu **"operation": "create"** si **"table": "nume_tabel"**
Se trimite perechea cheia **"values"** si un json ce contine ca si chei "coloanele" (structura tabelului), iar ca valori se trimite tipul dorit, **number** sau **text**  (vezi exemplu).

**Daca create-ul s-a efectuat cu succes, raspunsul va fi {"response": 1}.**

Daca create-ul s-a efectuat cu succes, vor fi create 2 fisiere in storage, *nume_tabel.json* (un fisier cu array empty) si *struct_nume_tabel.json* (acesta contine structura tabelul, adica ce se trimite in *values*).

## Exemplu de test:

```json
{
	"operation": "create",
	"table": "judete",
	"values": {
		"nume": "text",
		"suprafata": "number",
		"populatie": "number"
	}
}
```

- Traducere in sql: *CREATE TABLE judete (nume varchar, suprafata int, populatie int)*

## Posibile erori:

- In caz ca nu se trimite values, raspunsul va fi: **{ "response": -16}**
- In caz ca tipul trimis nu e **text** sau **number**, raspunsul va fi: **{ "response": -29}**
- Daca tabelul exista deja, raspunsul va fi: **{ "response": 0}**


# DROP

*-- Fisierele nume_tabel.json si struct_nume_tabel.json se sterg din storage!*

Se face request prin POST cu un json, cu **"operation": "drop"** si **"table": "nume_tabel"**

**Daca drop-ul s-a efectuat cu succes, raspunsul va fi {"response": 1}, altfel {"response": 0}.**

## Exemplu de test:

```json
{
	"operation": "drop",
	"table": "judete"
}
```

- Traducere in sql: *DROP TABLE judete;*


# Assertions

### Class Invariants

Avem urmatoarea metoda:
```java

private boolean areTableAndOperationOK() {
		
	if(table == null  || table.isEmpty() || operation == null || operation == RequestParser.OPERATION.INVALID_OPERATION) {
		return false;
	}
	return true;
		
}
```
Se verifica daca tabelul sau operatia efectuata pe tabel este invalida. Este obligatoriu ca acestea sa existe!

```java
public void requestController(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
		
	assert areTableAndOperationOK() : "The tabel or operation is invalid";
```


### Preconditions

**In AdmissionContestServlet sunt urmatoarele assert-uri:**

```java
public void requestController(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
	...
	if (requestParser.getCondition1() != null) {
			
		assert requestParser.getCondition1().op != RequestParser.OPERATOR.INVALID_OPERATOR: "The first operator is invalid";
			
		assert requestParser.getCondition1().key != null && !requestParser.getCondition1().key.isEmpty(): "The first key is invalid";
			
		assert requestParser.getCondition1().stringValue != null && !requestParser.getCondition1().stringValue.isEmpty(): "The first value is invalid";
		...
	}
	if (requestParser.getCondition2() != null) {
			
		assert requestParser.getCondition2().op != RequestParser.OPERATOR.INVALID_OPERATOR: "The second operator is invalid";
			
		assert requestParser.getCondition2().key != null && !requestParser.getCondition2().key.isEmpty(): "The second key is invalid";
			
		assert requestParser.getCondition2().stringValue != null && !requestParser.getCondition2().stringValue.isEmpty(): "The second value is invalid";
		...
	}
	...
	if (ao.fileExist(pathFile)) {
		jsonFile = ao.downloadJson(pathFile);
	} else {
			
		assert requestParser.getOperation() == RequestParser.OPERATION.CREATE: "The table does not exist";	
		...	
	}
	...
}
```
In primele 2 if-uri ne asiguram ca daca request-ul contine conditii, aceastea trebuie sa fie valide, iar in ultimul if ne asiguram ca tabelul exista pe server.

```java
public void handleInsert(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
	JSONObject values = requestParser.getValues();
		
	assert values != null : "Values are invalid";
	...
}
```
Ne asiguram ca valorile care urmeaza a fi inserate nu sunt null.


```java
public void handleInsertArray(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
	JSONArray values= requestParser.getValuesArray();
		
	assert values != null && values.length() != 0: "Values are invalid";
	...
}
```
Ne asiguram ca array-ul de valori care urmeaza a fi inserat nu este null sau empty.

```java
public void handleUpdate(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
	String jsonStructFile = "";
	
	if (requestParser.getTable().equals("students")) {
		jsonStructFile = Utils.structStudents;
	} else if (ao.fileExist(folder + "struct_" + requestParser.getTable() + ".json")) {
		jsonStructFile = ao.downloadJson(folder + "struct_" + requestParser.getTable() + ".json");
	}
		
	assert !jsonStructFile.isEmpty(): "Struct file does not exist";
	...
}
```
Ne asiguram ca structura tabelului exista (ce coloane are si de ce tip, number sau text).

```java
public void handleCreate(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
		
	JSONObject values = requestParser.getValues();
		
	assert values != null: "Values are not valid";
	...
```
Ne asiguram ca tabelul care urmeaza sa fie creat va avea coloane.

**In Utils avem urmatoarele assert-uri:**

```java
public static JSONArray getFilteredObjects(RequestParser requestParser, JSONArray jsonFile) {
	...
	if (requestParser.getCondition1() != null && requestParser.getCondition2() != null && requestParser.getLogicalCondition() != null) {
				
		assert conditionLogic != RequestParser.CONDITION_LOGIC.INVALID_CONDITION && 
			conditionLogic != RequestParser.CONDITION_LOGIC.NOT_NEEDED: "The logical condition is invalid";
		...		
	}
	...
}
```
Ne asiguram ca in cazul in care avem 2 conditii, conditia logica dintre aceasta trebuie sa fie AND sau OR. La fel si in metoda *getDifferenceFilteredObjects*

```java
public static JSONArray getUpdatedObjects(RequestParser requestParser, JSONArray jsonFile, JSONObject jsonStruct) {
			
	JSONObject values = requestParser.getValues();
	assert values != null : "The values are invalid";
```
Ne asiguram ca valorile obiectelor care urmeaza a fi modificate nu sunt null.


###Postconditions

**In AdmissionContestServlet avem urmatoarele assert-uri:**

```java
public void handleSelect(RequestParser requestParser, JSONArray jsonFile,  HttpServletResponse httpResponse, PrintWriter response) {
		
		JSONArray filteredObjects = Utils.getFilteredObjects(requestParser, jsonFile);
		
		assert (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) : "Error select";
		...
}
```
Ne asiguram ca ce vom returna nu este o eroare.


```java
public void handleInsert(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
	...
	assert !jsonFile.toString().isEmpty(): "Cannot insert empty students ";
	...
}
```
Ne asiguram ca obiectul care urmeaza sa fie inserat exista.

```java
public void handleUpdate(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
	...
	JSONArray updatedObjects = Utils.getUpdatedObjects(requestParser, jsonFile, jsonStruct);
	assert (!updatedObjects.toString().equals("[\"{\\\"response\\\": -16}\"]") 
				&& !updatedObjects.toString().equals("[\"{\\\"response\\\": -13}\"]") 
				&& !updatedObjects.toString().equals("[\"{\\\"response\\\": -16}\"]") 
				&& !updatedObjects.toString().equals("[\"{\\\"response\\\": -23}\"]") 
				&&  !updatedObjects.toString().equals("[\"{\\\"response\\\": -24}\"]") 
				&&  !updatedObjects.toString().equals("[\"{\\\"response\\\": -30}\"]")): "Error Update";
	...
}
```
Ne asiguram ca update-ul s-a efectuat cu succes si nu avem cod de eroare.

```java
public void handleDelete(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
	...
	JSONArray filteredObjects = Utils.getDifferenceFilteredObjects(requestParser, jsonFile);
	
	assert (!filteredObjects.toString().equals("[{\"response\":-13}]") && 
				!filteredObjects.toString().equals("[{\"response\":-23}]") && 
				!filteredObjects.toString().equals("[{\"response\":-24}]")) : "Error Delete";
	...
}
```
Ne asiguram ca delete-ul s-a efectuat cu succes si nu avem cod de eroare.

```java
public void handleCreate(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {

	...
	assert !values.toString().isEmpty(): "Error Create";
	...
}
```
Ne asiguram ca tabelul a fost creat.







