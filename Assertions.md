# Assertions

### Class Invariants

Avem următoarea metodă:
```java

private boolean areTableAndOperationOK() {
		
	if(table == null  || table.isEmpty() || operation == null || operation == RequestParser.OPERATION.INVALID_OPERATION) {
		return false;
	}
	return true;
		
}
```
Se verifică daca tabelul sau operația efectuată pe tabel este invalidă. Este obligatoriu ca acestea să existe!

```java
public void requestController(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
		
	assert areTableAndOperationOK() : "The tabel or operation is invalid";
```


### Preconditions

**În AdmissionContestServlet sunt următoarele assert-uri:**

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
În primele 2 if-uri ne asigurăm că dacă request-ul conține condiții, aceastea trebuie să fie valide, iar în ultimul if ne asigurăm că tabelul există pe server.

```java
public void handleInsert(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
	JSONObject values = requestParser.getValues();
		
	assert values != null : "Values are invalid";
	...
}
```
Ne asigurăm că valorile care urmează a fi inserate nu sunt null.


```java
public void handleInsertArray(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
		
	JSONArray values= requestParser.getValuesArray();
		
	assert values != null && values.length() != 0: "Values are invalid";
	...
}
```
Ne asigurăm că array-ul de valori care urmează a fi inserat nu este null sau empty.

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
Ne asigurăm că structura tabelului există (ce coloane are și de ce tip, number sau text).

```java
public void handleCreate(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {
		
	JSONObject values = requestParser.getValues();
		
	assert values != null: "Values are not valid";
	...
```
Ne asigurăm că tabelul care urmează să fie creat va avea coloane.

**În Utils avem următoarele assert-uri:**

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
Ne asigurăm că în cazul în care avem 2 condiții, condiția logică dintre aceasta trebuie să fie AND sau OR. La fel și în metoda *getDifferenceFilteredObjects()*

```java
public static JSONArray getUpdatedObjects(RequestParser requestParser, JSONArray jsonFile, JSONObject jsonStruct) {
			
	JSONObject values = requestParser.getValues();
	assert values != null : "The values are invalid";
```
Ne asigurăm că valorile obiectelor care urmează a fi modificate nu sunt null.


###Postconditions

**În AdmissionContestServlet avem următoarele assert-uri:**

```java
public void handleSelect(RequestParser requestParser, JSONArray jsonFile,  HttpServletResponse httpResponse, PrintWriter response) {
		
		JSONArray filteredObjects = Utils.getFilteredObjects(requestParser, jsonFile);
		
		assert (!filteredObjects.toString().equals("[{\"response\":-13}]") && !filteredObjects.toString().equals("[{\"response\":-23}]") && !filteredObjects.toString().equals("[{\"response\":-24}]")) : "Error select";
		...
}
```
Ne asigurăm că ce returnăm nu este o eroare.


```java
public void handleInsert(RequestParser requestParser, JSONArray jsonFile, String pathFile, HttpServletResponse httpResponse, PrintWriter response) {
	...
	assert !jsonFile.toString().isEmpty(): "Cannot insert empty students ";
	...
}
```
Ne asigurăm că obiectul care urmează să fie inserat există.

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
Ne asigurăm că update-ul s-a efectuat cu succes și nu avem cod de eroare.

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
Ne asigurăm că delete-ul s-a efectuat cu succes și nu avem cod de eroare.

```java
public void handleCreate(RequestParser requestParser, HttpServletResponse httpResponse, PrintWriter response) {

	...
	assert !values.toString().isEmpty(): "Error Create";
	...
}
```
Ne asigurăm ca tabelul a fost creat.
