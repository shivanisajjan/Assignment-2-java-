package com.stackroute.datamunger.query.parser;

import java.util.List;

/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */

public class QueryParameter {
	private String Filename;
	private String BaseQuery;
	private List<Restriction> Restrictions;
	private List<String> logical;
	private List<String> fields;
	private List<AggregateFunction> Aggregate;
	private List<String> groupby;
	private List<String> orderby;


	public void setFilename(String filename) {
		Filename = filename;
	}
	public void setBaseQuery(String baseQuery) {
		BaseQuery = baseQuery;
	}
	public void setRestrictions(List<Restriction> restrictions) {
		Restrictions = restrictions;
	}
	public void setLogical(List<String> logical) {
		this.logical = logical;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public void setAggregate(List<AggregateFunction> aggregate) {
		Aggregate = aggregate;
	}
	public void setGroupby(List<String> groupby) {
		this.groupby = groupby;
	}
	public void setorderby(List<String> orderby) {
		this.orderby = orderby;
	}






	public String getFileName() {
		return Filename;
	}

	public String getBaseQuery() {
		return BaseQuery;
	}

	public List<Restriction> getRestrictions() {
		return Restrictions;
	}

	public List<String> getLogicalOperators() {
		return logical;
	}

	public List<String> getFields() {
		return fields;
	}

	public List<AggregateFunction> getAggregateFunctions() {
		return Aggregate;
	}

	public List<String> getGroupByFields() {
		return groupby;
	}

	public List<String> getOrderByFields() {
		return orderby;
	}
}