package com.shursulei.plain.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Person {
	   private String fullName;
	   private String lastName;
	   private String description;
	   private String country;
	   private String company;
	   private String phone;
	   public String getDescription() {
	      return description;
	   }
	   public void setDescription(String description) {
	      this.description = description;
	   }
	   public String getFullName() {
	      return fullName;
	   }
	   public void setFullName(String fullName) {
	      this.fullName = fullName;
	   }

	   public String getLastName() {
	      return lastName;
	   }

	   public void setLastName(String lastName) {
	      this.lastName = lastName;
	   }

	   public String getCompany() {
	      return company;
	   }

	   public void setCompany(String company) {
	      this.company = company;
	   }

	   public String getCountry() {
	      return country;
	   }

	   public void setCountry(String country) {
	      this.country = country;
	   }

	   public String getPhone() {
	      return phone;
	   }

	   public void setPhone(String phone) {
	      this.phone = phone;
	   }

	   public boolean equals(Object obj) {
	      return EqualsBuilder.reflectionEquals(this, obj);
	   }

	   public int hashCode() {
	      return HashCodeBuilder
	         .reflectionHashCode(this);
	   }

	   public String toString() {
	      return ToStringBuilder.reflectionToString(
	         this, ToStringStyle.MULTI_LINE_STYLE);
	   }
}
