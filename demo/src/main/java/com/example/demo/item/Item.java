package com.example.demo.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All the details about the item.")
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_no", insertable = false)
	private Integer no;

	@Size(min = 2, message = "Name should have at least 2 characters")
	@ApiModelProperty(notes = "Name should have at least 2 characters")
	private String name;

	@Min(message = "Amount should be positive", value = 0)
	private Integer amount;

	@Column(name = "inventory_code", unique = true)
	@Min(message = "Inventory code should be positive", value = 0)
	private String inventoryCode;

	protected Item() {
		super();
	}

	public Item(Integer no, String name, Integer amount, String inventoryCode) {
		super();
		this.no = no;
		this.name = name;
		this.amount = amount;
		this.inventoryCode = inventoryCode;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	@Override
	public String toString() {
		return "Item [no=" + no + ", name=" + name + ", amount=" + amount + ", inventoryCode=" + inventoryCode + "]";
	}
}
