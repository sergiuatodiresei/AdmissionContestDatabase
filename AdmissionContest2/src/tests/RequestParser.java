package tests;
import org.json.JSONArray;
import org.json.JSONObject;

public class RequestParser {

	private static final String JVAL_SELECT = "select";
	private static final String JVAL_INSERT = "insert";
	private static final String JVAL_INSERT_ARRAY = "insert_array";
	private static final String JVAL_DELETE = "delete";
	private static final String JVAL_UPDATE = "update";
	private static final String JVAL_CREATE = "create";
	private static final String JVAL_DROP = "drop";
	private static final String JVAL_AND = "and";
	private static final String JVAL_OR = "or";
	
	private static final String JVAL_G = ">";
	private static final String JVAL_E = "=";
	private static final String JVAL_L = "<";
	private static final String JVAL_GE = ">=";
	private static final String JVAL_LE = "<=";
	
	private static final String JKEY_OPERATION = "operation";
	private static final String JKEY_OPERATOR = "operator";
	private static final String JKEY_FROM = "from";
	private static final String JKEY_INTO = "into";
	private static final String JKEY_TABLE = "table";
	private static final String JKEY_CONDITION = "logical_condition";
	private static final String JKEY_COND1 = "cond1";
	private static final String JKEY_COND2 = "cond2";
	private static final String JKEY_KEY = "key";
	private static final String JKEY_VALUE = "value";
	
	private OPERATION operation;
	private String table;
	private CONDITION_LOGIC logicalCondition;
	private Condition condition1;
	private Condition condition2;
	private JSONObject values;
	private JSONArray valuesArray;
	
	public enum OPERATION {
		SELECT,INSERT,INSERT_ARRAY,UPDATE,DELETE,CREATE, DROP, INVALID_OPERATION	
	}
	
	public enum CONDITION_LOGIC {
		AND, OR, INVALID_CONDITION, NOT_NEEDED
	}
	
	public enum OPERATOR {
		G, E, L, GE, LE, INVALID_OPERATOR
	}
	
	public class Condition {
		
		public String key;
		public OPERATOR op;
		public String stringValue = "";
		
		public Condition(String key, OPERATOR op, String stringValue) {
			this.key = key;
			this.op = op;
			this.stringValue = stringValue;
		}
	
	}
	
	public void parse(JSONObject input) {
		operation = handleOperation(input.optString(JKEY_OPERATION));
		if (input.optString(JKEY_FROM) != null && !input.optString(JKEY_FROM).isEmpty()) {
			table = input.optString(JKEY_FROM);
		} else if (input.optString(JKEY_INTO) != null && !input.optString(JKEY_INTO).isEmpty()) {
			table = input.optString(JKEY_INTO);
		} else {
			table = input.optString(JKEY_TABLE);
		}
		
		JSONObject cond1 = input.optJSONObject(JKEY_COND1);
		if (cond1 != null) {
			condition1 = new Condition(
					cond1.optString(JKEY_KEY),
					handleOperator(cond1.optString(JKEY_OPERATOR)),
					cond1.optString(JKEY_VALUE));
		}
		JSONObject cond2 = input.optJSONObject(JKEY_COND2);
		if (cond2 != null) {
			condition2 = new Condition(
					cond2.optString(JKEY_KEY),
					handleOperator(cond2.optString(JKEY_OPERATOR)),
					cond2.optString(JKEY_VALUE));
		}
		logicalCondition = (condition1 != null && condition2 != null) ? handleLogicalCondition(input.optString(JKEY_CONDITION)) : CONDITION_LOGIC.NOT_NEEDED;

		values = input.optJSONObject("values");
		valuesArray = input.optJSONArray("values_array");
	}
	
	public OPERATION handleOperation(String op) {
		switch (op) {
			case JVAL_SELECT: return OPERATION.SELECT;
			case JVAL_INSERT: return OPERATION.INSERT;
			case JVAL_INSERT_ARRAY: return OPERATION.INSERT_ARRAY;
			case JVAL_UPDATE: return OPERATION.UPDATE;
			case JVAL_DELETE: return OPERATION.DELETE;
			case JVAL_CREATE: return OPERATION.CREATE;
			case JVAL_DROP: return OPERATION.DROP;
			default: return OPERATION.INVALID_OPERATION;
		}
	}
	
	public CONDITION_LOGIC handleLogicalCondition(String cond) {
		switch (cond) {
			case JVAL_AND: return CONDITION_LOGIC.AND;
			case JVAL_OR: return CONDITION_LOGIC.OR;
			default: return CONDITION_LOGIC.INVALID_CONDITION;
		}
	}
	
	public OPERATOR handleOperator(String operator) {
		switch (operator) {
		case JVAL_G: return OPERATOR.G;
		case JVAL_E: return OPERATOR.E;
		case JVAL_L: return OPERATOR.L;
		case JVAL_GE: return OPERATOR.GE;
		case JVAL_LE: return OPERATOR.LE;
		default: return OPERATOR.INVALID_OPERATOR;
		}
	}
	
	public OPERATION getOperation() {
		return operation;
	}
	
	public String getTable() {
		return table;
	}
	
	public CONDITION_LOGIC getLogicalCondition() {
		return logicalCondition;
	}
	
	public Condition getCondition1() {
		return condition1;
	}
	
	public Condition getCondition2() {
		return condition2;
	}
	
	public JSONObject getValues() {
		return values;
	}
	
	public JSONArray getValuesArray() {
		return valuesArray;
	}
}
