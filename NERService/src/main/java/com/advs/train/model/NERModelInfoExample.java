package com.advs.train.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NERModelInfoExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public NERModelInfoExample() {
		oredCriteria = new ArrayList<>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}

		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}

		public Criteria andIdEqualTo(String value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(String value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(String value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(String value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(String value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(String value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLike(String value) {
			addCriterion("id like", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotLike(String value) {
			addCriterion("id not like", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<String> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<String> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(String value1, String value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(String value1, String value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andNameIsNull() {
			addCriterion("name is null");
			return (Criteria) this;
		}

		public Criteria andNameIsNotNull() {
			addCriterion("name is not null");
			return (Criteria) this;
		}

		public Criteria andNameEqualTo(String value) {
			addCriterion("name =", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotEqualTo(String value) {
			addCriterion("name <>", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameGreaterThan(String value) {
			addCriterion("name >", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameGreaterThanOrEqualTo(String value) {
			addCriterion("name >=", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLessThan(String value) {
			addCriterion("name <", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLessThanOrEqualTo(String value) {
			addCriterion("name <=", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameLike(String value) {
			addCriterion("name like", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotLike(String value) {
			addCriterion("name not like", value, "name");
			return (Criteria) this;
		}

		public Criteria andNameIn(List<String> values) {
			addCriterion("name in", values, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotIn(List<String> values) {
			addCriterion("name not in", values, "name");
			return (Criteria) this;
		}

		public Criteria andNameBetween(String value1, String value2) {
			addCriterion("name between", value1, value2, "name");
			return (Criteria) this;
		}

		public Criteria andNameNotBetween(String value1, String value2) {
			addCriterion("name not between", value1, value2, "name");
			return (Criteria) this;
		}

		public Criteria andLocationIsNull() {
			addCriterion("location is null");
			return (Criteria) this;
		}

		public Criteria andLocationIsNotNull() {
			addCriterion("location is not null");
			return (Criteria) this;
		}

		public Criteria andLocationEqualTo(String value) {
			addCriterion("location =", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationNotEqualTo(String value) {
			addCriterion("location <>", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationGreaterThan(String value) {
			addCriterion("location >", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationGreaterThanOrEqualTo(String value) {
			addCriterion("location >=", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationLessThan(String value) {
			addCriterion("location <", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationLessThanOrEqualTo(String value) {
			addCriterion("location <=", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationLike(String value) {
			addCriterion("location like", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationNotLike(String value) {
			addCriterion("location not like", value, "location");
			return (Criteria) this;
		}

		public Criteria andLocationIn(List<String> values) {
			addCriterion("location in", values, "location");
			return (Criteria) this;
		}

		public Criteria andLocationNotIn(List<String> values) {
			addCriterion("location not in", values, "location");
			return (Criteria) this;
		}

		public Criteria andLocationBetween(String value1, String value2) {
			addCriterion("location between", value1, value2, "location");
			return (Criteria) this;
		}

		public Criteria andLocationNotBetween(String value1, String value2) {
			addCriterion("location not between", value1, value2, "location");
			return (Criteria) this;
		}

		public Criteria andVersionIsNull() {
			addCriterion("version is null");
			return (Criteria) this;
		}

		public Criteria andVersionIsNotNull() {
			addCriterion("version is not null");
			return (Criteria) this;
		}

		public Criteria andVersionEqualTo(Integer value) {
			addCriterion("version =", value, "version");
			return (Criteria) this;
		}

		public Criteria andVersionNotEqualTo(Integer value) {
			addCriterion("version <>", value, "version");
			return (Criteria) this;
		}

		public Criteria andVersionGreaterThan(Integer value) {
			addCriterion("version >", value, "version");
			return (Criteria) this;
		}

		public Criteria andVersionGreaterThanOrEqualTo(Integer value) {
			addCriterion("version >=", value, "version");
			return (Criteria) this;
		}

		public Criteria andVersionLessThan(Integer value) {
			addCriterion("version <", value, "version");
			return (Criteria) this;
		}

		public Criteria andVersionLessThanOrEqualTo(Integer value) {
			addCriterion("version <=", value, "version");
			return (Criteria) this;
		}

		public Criteria andVersionIn(List<Integer> values) {
			addCriterion("version in", values, "version");
			return (Criteria) this;
		}

		public Criteria andVersionNotIn(List<Integer> values) {
			addCriterion("version not in", values, "version");
			return (Criteria) this;
		}

		public Criteria andVersionBetween(Integer value1, Integer value2) {
			addCriterion("version between", value1, value2, "version");
			return (Criteria) this;
		}

		public Criteria andVersionNotBetween(Integer value1, Integer value2) {
			addCriterion("version not between", value1, value2, "version");
			return (Criteria) this;
		}

		public Criteria andCreateDateIsNull() {
			addCriterion("create_date is null");
			return (Criteria) this;
		}

		public Criteria andCreateDateIsNotNull() {
			addCriterion("create_date is not null");
			return (Criteria) this;
		}

		public Criteria andCreateDateEqualTo(Date value) {
			addCriterion("create_date =", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateNotEqualTo(Date value) {
			addCriterion("create_date <>", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateGreaterThan(Date value) {
			addCriterion("create_date >", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
			addCriterion("create_date >=", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateLessThan(Date value) {
			addCriterion("create_date <", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateLessThanOrEqualTo(Date value) {
			addCriterion("create_date <=", value, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateIn(List<Date> values) {
			addCriterion("create_date in", values, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateNotIn(List<Date> values) {
			addCriterion("create_date not in", values, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateBetween(Date value1, Date value2) {
			addCriterion("create_date between", value1, value2, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateDateNotBetween(Date value1, Date value2) {
			addCriterion("create_date not between", value1, value2, "createDate");
			return (Criteria) this;
		}

		public Criteria andCreateUserIsNull() {
			addCriterion("create_user is null");
			return (Criteria) this;
		}

		public Criteria andCreateUserIsNotNull() {
			addCriterion("create_user is not null");
			return (Criteria) this;
		}

		public Criteria andCreateUserEqualTo(String value) {
			addCriterion("create_user =", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserNotEqualTo(String value) {
			addCriterion("create_user <>", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserGreaterThan(String value) {
			addCriterion("create_user >", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
			addCriterion("create_user >=", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserLessThan(String value) {
			addCriterion("create_user <", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserLessThanOrEqualTo(String value) {
			addCriterion("create_user <=", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserLike(String value) {
			addCriterion("create_user like", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserNotLike(String value) {
			addCriterion("create_user not like", value, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserIn(List<String> values) {
			addCriterion("create_user in", values, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserNotIn(List<String> values) {
			addCriterion("create_user not in", values, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserBetween(String value1, String value2) {
			addCriterion("create_user between", value1, value2, "createUser");
			return (Criteria) this;
		}

		public Criteria andCreateUserNotBetween(String value1, String value2) {
			addCriterion("create_user not between", value1, value2, "createUser");
			return (Criteria) this;
		}

		public Criteria andConfigIsNull() {
			addCriterion("config is null");
			return (Criteria) this;
		}

		public Criteria andConfigIsNotNull() {
			addCriterion("config is not null");
			return (Criteria) this;
		}

		public Criteria andConfigEqualTo(String value) {
			addCriterion("config =", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigNotEqualTo(String value) {
			addCriterion("config <>", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigGreaterThan(String value) {
			addCriterion("config >", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigGreaterThanOrEqualTo(String value) {
			addCriterion("config >=", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigLessThan(String value) {
			addCriterion("config <", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigLessThanOrEqualTo(String value) {
			addCriterion("config <=", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigLike(String value) {
			addCriterion("config like", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigNotLike(String value) {
			addCriterion("config not like", value, "config");
			return (Criteria) this;
		}

		public Criteria andConfigIn(List<String> values) {
			addCriterion("config in", values, "config");
			return (Criteria) this;
		}

		public Criteria andConfigNotIn(List<String> values) {
			addCriterion("config not in", values, "config");
			return (Criteria) this;
		}

		public Criteria andConfigBetween(String value1, String value2) {
			addCriterion("config between", value1, value2, "config");
			return (Criteria) this;
		}

		public Criteria andConfigNotBetween(String value1, String value2) {
			addCriterion("config not between", value1, value2, "config");
			return (Criteria) this;
		}

		public Criteria andModifiedDateIsNull() {
			addCriterion("modified_date is null");
			return (Criteria) this;
		}

		public Criteria andModifiedDateIsNotNull() {
			addCriterion("modified_date is not null");
			return (Criteria) this;
		}

		public Criteria andModifiedDateEqualTo(Date value) {
			addCriterion("modified_date =", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateNotEqualTo(Date value) {
			addCriterion("modified_date <>", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateGreaterThan(Date value) {
			addCriterion("modified_date >", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateGreaterThanOrEqualTo(Date value) {
			addCriterion("modified_date >=", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateLessThan(Date value) {
			addCriterion("modified_date <", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateLessThanOrEqualTo(Date value) {
			addCriterion("modified_date <=", value, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateIn(List<Date> values) {
			addCriterion("modified_date in", values, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateNotIn(List<Date> values) {
			addCriterion("modified_date not in", values, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateBetween(Date value1, Date value2) {
			addCriterion("modified_date between", value1, value2, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedDateNotBetween(Date value1, Date value2) {
			addCriterion("modified_date not between", value1, value2, "modifiedDate");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIsNull() {
			addCriterion("modified_user is null");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIsNotNull() {
			addCriterion("modified_user is not null");
			return (Criteria) this;
		}

		public Criteria andModifiedUserEqualTo(String value) {
			addCriterion("modified_user =", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNotEqualTo(String value) {
			addCriterion("modified_user <>", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserGreaterThan(String value) {
			addCriterion("modified_user >", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserGreaterThanOrEqualTo(String value) {
			addCriterion("modified_user >=", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserLessThan(String value) {
			addCriterion("modified_user <", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserLessThanOrEqualTo(String value) {
			addCriterion("modified_user <=", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserLike(String value) {
			addCriterion("modified_user like", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNotLike(String value) {
			addCriterion("modified_user not like", value, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserIn(List<String> values) {
			addCriterion("modified_user in", values, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNotIn(List<String> values) {
			addCriterion("modified_user not in", values, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserBetween(String value1, String value2) {
			addCriterion("modified_user between", value1, value2, "modifiedUser");
			return (Criteria) this;
		}

		public Criteria andModifiedUserNotBetween(String value1, String value2) {
			addCriterion("modified_user not between", value1, value2, "modifiedUser");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table ner_model_info
	 * @mbg.generated  Mon Apr 19 17:27:41 PDT 2021
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table ner_model_info
     *
     * @mbg.generated do_not_delete_during_merge Mon Apr 19 00:05:09 PDT 2021
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }
}