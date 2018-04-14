# AdmissionContestDatabase

**URL API**

## http://admissioncontest.us-east-2.elasticbeanstalk.com/AdmissionContestServlet

URL fiser *students.json*:

###  https://s3.amazonaws.com/admission-contest/database/students.json

## SELECT

Se face request prin post cu un json, cu maxim 2 conditii ("cond1", "cond2").

### Cheia "key" reprezinta "coloana" din tabel si poate avea urmatoarele valori pentru tabelul *students*:
- **id**
- **first_name**
- **last_name**
- **medie_bac**
- **nota_examen**
- **medie**


### Cheia "operation" poate avea urmatoarele valori:
- **=**
- **<**
- **>**
- **<=** 
- **>=**


### Cheia "logical_condition" reprezinta conditia dintre *cond1* si *cond2* poate avea urmatoarele valori:
- **AND**
- **OR**


## Exemple de teste:

### Ex. 1: 

{
	"operation": "select",
	"from": "students"
}

- Traducere in sql: *SELECT * FROM students*


### Ex. 2: 

{
	"operation": "select",
	"from": "students",
	"cond1": {
		"key": "id",
		"operation": "<=",
		"value": 10
		}
}

- Traducere in sql: *SELECT * FROM students WHERE id <= 10*

### Ex. 3: 

{
	"operation": "select",
	"from": "students",
	"cond1": {
		"key": "id",
		"operation": "=",
		"value": "1"
		},
	"cond2": {
		"key": "first_name",
		"operation": "=",
		"value": "Mircea"
		},
	"logical_condition": "or"
}

- Traducere in sql: *SELECT * FROM students WHERE id <= 10 OR first_name = 'Mircea'*
