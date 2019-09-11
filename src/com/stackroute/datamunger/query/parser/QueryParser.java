package com.stackroute.datamunger.query.parser;
/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

import java.util.ArrayList;
import java.util.List;

public class QueryParser {


	private QueryParameter queryParameter = new QueryParameter();

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		queryParameter.setFilename(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setorderby(getOrderByFields(queryString));
		queryParameter.setGroupby(getGroupByFields(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setLogical(getLogicalOperators(queryString));
		queryParameter.setAggregate(getAggregateFunctions(queryString));
		queryParameter.setRestrictions(getConditions(queryString));
		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */

	public String getFileName(String queryString) {
		int flag=0;
		String string_return="";
		String str[]= queryString.toLowerCase().split(" ");
		for(int i=0;i<str.length && flag==0;i++) {
			if (str[i].equals("from")) {
				string_return = str[i + 1];
				flag = 1;
			}
		}
		return string_return;
	}

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */

	public String getBaseQuery(String queryString) {
		String str[]= queryString.toLowerCase().split(" where| order by| group by");
		return str[0];
	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */


	public List<String> getOrderByFields(String queryString) {
		String str[]= queryString.toLowerCase().split("order by ");
		String string_init[]={};
		List<String> l=new ArrayList<>();
		try{
			string_init=str[1].toLowerCase().split(",");
			for(int i=0;i<string_init.length;i++){
				l.add(string_init[i]);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		}
		return l;
	}


	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */

	public List<String> getGroupByFields(String queryString) {
		String str[]= queryString.toLowerCase().split("group by ");
		String string_array[]={};
		List<String> l=new ArrayList<>();
		try{
			String str1[]= str[1].toLowerCase().split(" order by");
			string_array=str1[0].toLowerCase().split(",");
			for(int i=0;i<string_array.length;i++){
				l.add(string_array[i]);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		return l;
	}

	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */

	public List<String> getFields(String queryString) {
		List<String> l=new ArrayList<>();
		String string_return[]= queryString.toLowerCase().split(" ");
		String[] string_array=string_return[1].toLowerCase().split(",");;
		for(int i=0;i<string_array.length;i++){
			l.add(string_array[i]);
		}
		return l;
	}


	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */

	public List<Restriction> getConditions(String queryString) {
		String str[]= queryString.split("where ");
		String str_get[]={};
		List<Restriction> l=new ArrayList<>();
		String[] words = {"<=", ">=", "!=", "<", ">","="};
		try {
			String string_temp[] = str[1].split(" group by| order by");
			str_get = string_temp[0].split(" and | or ");
			for (int i = 0; i < str_get.length; i++) {
				for (String word : words) {
					if (str_get[i].contains(word)) {
						String str1[] = str_get[i].split(word);
						if(str1[1].contains("'")){
							String str2[] = str1[1].split("'");
							l.add(new Restriction(str1[0].trim(),str2[1].trim(),word));
						}
						else
						l.add(new Restriction(str1[0].trim(),str1[1].trim(),word));
					}
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		return l;
	}

	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */


	public List<String> getLogicalOperators(String queryString) {
		String str[]= queryString.toLowerCase().split("where ");
		int flag=1;
		ArrayList<String>  array_temp = new ArrayList<String>();
		try{
			String string_inter[]=str[1].toLowerCase().split(" group by| order by");
			String string1[] =string_inter[0].toLowerCase().split(" ");
			for(int i=0;i<string1.length;i++){
				if(string1[i].equals("and") || string1[i].equals("or")){
					array_temp.add(string1[i]);
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		return array_temp;
	}


	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */


	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		String str[] = queryString.toLowerCase().split("select | from");
		List<AggregateFunction> aggregating = new ArrayList<AggregateFunction>();
		String[] words = {"count", "sum", "min", "max", "avg"};
		String string_sep[] = str[1].toLowerCase().split(",");
		int flag = 1;
		for (int i = 0; i < string_sep.length; i++) {
			boolean isFound = string_sep[i].contains("(");
			if (isFound) {
				for (String word : words) {
					if (string_sep[i].contains(word)) {
						String str1[] = string_sep[i].toLowerCase().split("\\(|\\)");
						aggregating.add(new AggregateFunction(str1[1],word));
					}
				}
			}
		}
		return aggregating;
	}
}