package compensation.domain;

import compensation.OrderApplication;
import compensation.domain.OrderCancelled;
import compensation.domain.OrderPlaced;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Order_table")
@Data
//<<< DDD / Aggregate Root
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productId;

    private Integer qty;

    private String customerId;

    private Double amount;

    private String status;

    private String address;

    @PostPersist
    public void onPostPersist() {
        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();

        // OrderCancelled orderCancelled = new OrderCancelled(this);
        // orderCancelled.publishAfterCommit();
        // Get request from Order
        //compensation.external.Order order =
        //    Application.applicationContext.getBean(compensation.external.OrderService.class)
        //    .getOrder(/** mapping value needed */);

    }

    @PreRemove
    public void onPreRemove() {}

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(
            OrderRepository.class
        );
        return orderRepository;
    }

    public static void updateStatus(OutOfStock outOfStock) {
        repository().findById(outOfStock.getOrderId()).ifPresent(order ->{
            order.setStatus("OrderCancelled");
            repository().save(order);
        });

    }

}
//>>> DDD / Aggregate Root
