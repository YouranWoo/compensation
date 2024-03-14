package compensation.domain;

import compensation.InventoryApplication;
import compensation.domain.OutOfStock;
import compensation.domain.StockDecreased;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Inventory_table")
@Data
//<<< DDD / Aggregate Root
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long stock;

    // @PostPersist
    // public void onPostPersist() {
    //     OutOfStock outOfStock = new OutOfStock(this);
    //     outOfStock.publishAfterCommit();
    // }

    // @PostUpdate
    // public void onPostUpdate() {
    //     StockDecreased stockDecreased = new StockDecreased(this);
    //     stockDecreased.publishAfterCommit();
    // }

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    public static void decreaseStock(OrderPlaced orderPlaced) {

        repository().findById(Long.valueOf(orderPlaced.getProductId())).ifPresent(inventory->{
            if(inventory.getStock() >= orderPlaced.getQty()){
                inventory.setStock(inventory.getStock() - orderPlaced.getQty()); 
                repository().save(inventory);

                StockDecreased stockDecreased = new StockDecreased(inventory);
                stockDecreased.publishAfterCommit();

            }else{
                OutOfStock outOfStock = new OutOfStock(inventory);
                outOfStock.setOrderId(orderPlaced.getId()); 
                outOfStock.publishAfterCommit();
            }
            
        });
        
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void increaseStock(OrderCancelled orderCancelled) {
        //implement business logic here:

        /** Example 1:  new item 
        Inventory inventory = new Inventory();
        repository().save(inventory);

        */

        /** Example 2:  finding and process
        
        repository().findById(orderCancelled.get???()).ifPresent(inventory->{
            
            inventory // do something
            repository().save(inventory);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
