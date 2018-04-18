# AdmissionContestDatabase

**URL API**

## http://admissioncontest.us-east-2.elasticbeanstalk.com/AdmissionContestServlet

URL fiser *students.json*:

###  https://s3.amazonaws.com/admission-contest/database/students.json

URL fiser *struct_students.json*:

###  https://s3.amazonaws.com/admission-contest/database/struct_students.json

URL fiser *criterii_insert_students.json*:

###  https://s3.amazonaws.com/admission-contest/database/criterii_insert_students.json



### Posibile erori generale:
- In caz ca nu se citeste bine json-ul primit, raspunsul va fi: **{ "response": -1}**
- In caz ca nu se citeste bine un fisier din s3 (amazon) sau tabelul(fisierul) nu exista, raspunsul va fi: **{ "response": -10}**
- In caz ca nu este primit in json numele tabelui si/sau operatia ceruta, raspunsul va fi : **{ "response": -11}**
- In caz ca se trimite o operatie gresita (*operation*), raspunsul va fi: **{ "response": -12}**
In cadrul unei conditii:
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

## Pentru a specifica tabelul pot fi trimise urmatoarle valori:
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

- Traducere in sql: *SELECT * FROM students WHERE id <= 10 OR first_name = 'Mircea'*


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
- In caz ca nu s-a putut citi tabelul "criterii_insert_json" raspunsul va fi: **{ "response": -17}**
- In caz ca nu se trimite medie_bac, raspunsul va fi: **{ "response": -18}**
- In caz ca nu se trimite nota_examen, raspunsul va fi: **{ "response": -19}**
- In caz ca pentru medie_bac se trimit alte valori din intervalul [5,10], raspunsul va fi: **{ "response": -20}**
- In caz ca pentru nota_examen se trimit alte valori din intervalul [0,10], raspunsul va fi: **{ "response": -21}**
- Alte erori - **{ "response": -22}**


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
Se trimite perechea cheia **"values"** si un json ce contine ca si chei "coloanele" (structura tabelului), iar ca valori ""  (Vezi exemplu)

**ID-ul nu se trimite (el se autoincrementeaza).**

**Daca create-ul s-a efectuat cu succes, raspunsul va fi {"response": 1}.**

Daca create-ul s-a efectuat cu succes, vor fi create 2 fisiere in storage, *nume_tabel.json* (un fisier cu array empty) si *struct_nume_tabel.json* (acesta contine structura tabelul, adica ce se trimite in *values*.

## Exemplu de test:

```json
{
	"operation": "create",
	"table": "judete",
	"values": {
		"nume": "",
		"suprafata": "",
		"populatie": ""
	}
}
```

- Traducere in sql: *CREATE TABLE judete (nume varchar, suprafata int, populatie int)*

## Posibile erori:

- In caz ca nu se trimite values, raspunsul va fi: **{ "response": -16}**
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
