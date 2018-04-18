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

## SELECT

Se face request prin POST cu un json, cu **maxim** 2 conditii ("cond1", "cond2").
Se trimite **"operation": "select"** si **"from": "nume_tabel"**

### Cheia "key" reprezinta "coloana" din tabel si poate avea urmatoarele valori pentru tabelul *students*:
- **id**
- **first_name**
- **last_name**
- **medie_bac**
- **nota_examen**
- **medie**


### Cheia "operator" poate avea urmatoarele valori:
- **=**
- **<**
- **>**
- **<=** 
- **>=**


### Cheia "logical_condition" reprezinta conditia dintre *cond1* si *cond2* si poate avea urmatoarele valori:
- **AND**
- **OR**


## Exemple de teste:

### Ex. 1: 

```javascript
{
	"operation": "select",
	"from": "students"
}
```
- Traducere in sql: *SELECT * FROM students*


### Ex. 2: 
```perl
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

- Traducere in sql: *SELECT * FROM students WHERE id <= 10 OR first_name = 'Mircea'*


## INSERT

Se face request prin POST cu un json, cu **"operation": "insert"** si **"into": "nume_tabel"**
Se trimite perechea cheia **"values"** si un json ce contine "coloanele" (Vezi exemplu)
ID-ul nu se trimite (el se autoincrementeaza). Nu e necesar sa fie trimise toate coloanele. Coloanele lipsa vor adaugate automat cu valoarea "".
Pentru tabelul students, media nu trebuie trimisa, se calculeaza automat pe baza datelor din tabelul **criterii_insert_students**, dar **"medie_bac"** si **"nota_examen"** trebuie.

**Daca insertul s-a efectuat cu succes, raspunsul va fi {"response": 1}**

## Exemple de teste:

### Ex. 1: 

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

- Traducere in sql: *INSERT INTO students VALUES("Prenume_student", "Nume_student", 10, 9)*


### Ex. 2: 

{
	"operation": "insert",
	"into": "students",
	"values": {
		"first_name": "Prenume_student",
		"medie_bac": 10,
		"nota_examen": 9
	}
}

- Traducere in sql: *INSERT INTO students VALUES("Prenume_student", "", 10, 9)*

## Posibile erori:

- In caz ca nu se trimite values, raspunsul va fi: **{ "response": -16}**
- In caz ca nu s-a putut citi tabelul "criterii_insert_json" raspunsul va fi: **{ "response": -17}**
- In caz ca nu se trimite medie_bac, raspunsul va fi: **{ "response": -18}**
- In caz ca nu se trimite nota_examen, raspunsul va fi: **{ "response": -19}**
- In caz ca pentru medie_bac se trimit alte valori din intervalul [5,10], raspunsul va fi: **{ "response": -20}**
- In caz ca pentru nota_examen se trimit alte valori din intervalul [0,10], raspunsul va fi: **{ "response": -21}**
- Alte erori - **{ "response": -21}**
