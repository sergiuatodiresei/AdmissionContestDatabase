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
