package com.example.demo.item;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.annotations.ApiOperation;


@RestController
public class ItemJPAResource {

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping("/items")
	@ApiOperation(value = "List of the inventory items list", notes = "Find all the items in the inventory", response = Item.class)
	public List<Item> retrieveAllUsers() {
		return itemRepository.findAll();
	}

	@GetMapping("/items/{no}")
	@ApiOperation(value = "Read item details by item no", notes = "Find a specific item by the item's no")
	public EntityModel<Item> retrieveUser(@PathVariable int no) {
		Optional<Item> item = itemRepository.findById(no);
		if (!item.isPresent())
			throw new ItemNotFoundException("Item no-" + no + " not found");
		EntityModel<Item> resource = EntityModel.of(item.get());
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		resource.add(linkTo.withRel("all-items"));
		return resource;
	}

	@PutMapping("/items/{no}/withdrawal/{quantity}")
	@ApiOperation(value = "Withdrawal quantity of a specific item from stock", notes = "Make a withdrawal of the item assuming that quantity exists in the inventory and return it with the new quantity", response = Item.class)
	public Item withdrawalItem(@PathVariable int no, @PathVariable int quantity) {
		Optional<Item> item = itemRepository.findById(no);
		Integer currQuantity = item.get().getAmount();
		if (currQuantity < quantity)
			throw new ItemNotFoundException("The quantity " + quantity
					+ " cannot be withdrawn. The quantity of items available in stock is " + item.get().getAmount());
		else if (quantity < 0)
			throw new ItemNotFoundException("The amount of withdrawal should be positive");
		int newQuantity = currQuantity - quantity;
		item.get().setAmount(newQuantity);
		return itemRepository.save(item.get());
	}

	@PutMapping("/items/{no}/deposit/{quantity}")
	@ApiOperation(value = "Deposit quantity of a specific item to stock", notes = "Make a deposit of an item and return it with the new quantity", response = Item.class)
	public Item depositItem(@PathVariable int no, @PathVariable int quantity) {
		Optional<Item> item = itemRepository.findById(no);
		if (quantity < 0)
			throw new ItemNotFoundException("The amount of deposit should be positive");
		int currQuantity = item.get().getAmount();
		int newQuantity = currQuantity + quantity;
		item.get().setAmount(newQuantity);
		return itemRepository.save(item.get());
	}

	@DeleteMapping("/items/{no}")
	@ApiOperation(value = "Delete an item from stock", notes = "Delete an item and return it", response = Item.class)
	public Optional<Item> deleteUser(@PathVariable int no) {
		Optional<Item> item = itemRepository.findById(no);
		itemRepository.deleteById(no);
		return item;
	}

	@PostMapping("/items")
	@ApiOperation(value = "Add an items to stock", notes = "Add an item to the stock from the request body", response = Item.class)
	public ResponseEntity<Object> createUser(@Valid @RequestBody Item item) {
		for (Item items : itemRepository.findAll()) {
			if (items.getInventoryCode().equals(item.getInventoryCode()))
				throw new ItemNotFoundException(
						"Inventory code-" + item.getNo() + " already exists for item no-" + item.getNo());

			if (items.getNo().equals(item.getNo()))
				throw new ItemNotFoundException("Item no-" + item.getNo() + " already exists");
		}
		Item saveUser = itemRepository.save(item);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveUser.getNo())
				.toUri();
		return ResponseEntity.created(location).build();
	}

}
