package csc212.copy;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        MyLinkedList<Product> products = new MyLinkedList<>();
        MyLinkedList<Customer> customers = new MyLinkedList<>();
        MyLinkedList<Order> orders = new MyLinkedList<>();

        System.out.println("Loading data...");
        Data.loadProducts(products);
        Data.loadCustomers(customers);
        Data.loadOrders(orders, customers, products);
        Data.loadReviews(products, customers);
        System.out.println("All data loaded successfully!\n");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (true) {
            System.out.println("===== E-Commerce Inventory System =====");
            System.out.println("1. View All Products");
            System.out.println("2. View All Customers");
            System.out.println("3. View All Orders");
            System.out.println("4. View All Products with Average Ratings");
            System.out.println("5. Edit Product");
            System.out.println("6. Remove Product");
            System.out.println("7. Edit Customer");
            System.out.println("8. Remove Customer");
            System.out.println("9. Edit Order");
            System.out.println("10. Remove Order");
            System.out.println("11. Edit Review");
            System.out.println("12. Add Product");
            System.out.println("13. Add Customer");
            System.out.println("14. Place Order");
            System.out.println("15. Add Review");
            System.out.println("16. View Customer Order History");
            System.out.println("17. View Out-of-Stock Products");
            System.out.println("18. View Top 3 Products by Rating");
            System.out.println("19. View Orders Between Dates");
            System.out.println("20. View Common High-Rated Products");
            System.out.println("21. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.\n");
                scanner.next();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    printAllProducts(products);
                    break;
                case 2:
                    printAllCustomers(customers);
                    break;
                case 3:
                    printAllOrders(orders);
                    break;
                case 4:
                    printAllProductsWithRatings(products);
                    break;
                case 5:
                    editProduct(products, scanner);
                    break;
                case 6:
                    removeProduct(products, scanner);
                    break;
                case 7:
                    editCustomer(customers, scanner);
                    break;
                case 8:
                    removeCustomer(customers, scanner);
                    break;
                case 9:
                    editOrder(orders, scanner, customers, products);
                    break;
                case 10:
                    removeOrder(orders, scanner, customers);
                    break;
                case 11:
                    editReview(products, scanner);
                    break;
                case 12:
                    addProductInteraction(products, scanner);
                    break;
                case 13:
                    addCustomerInteraction(customers, scanner);
                    break;
                case 14:
                    placeOrderInteraction(orders, scanner, customers, products);
                    break;
                case 15:
                    addReviewInteraction(products, scanner, customers);
                    break;
                case 16:
                    printOrderHistoryInteraction(customers, scanner);
                    break;
                case 17:
                    printOutOfStock(products);
                    break;
                case 18:
                    printTopRatedProducts(products);
                    break;
                case 19:
                    printOrdersBetweenDates(orders, scanner);
                    break;
                case 20:
                    printCommonHighRatedProducts(products, scanner, customers);
                    break;
                case 21:
                    System.out.println("Exiting system... Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please select again.\n");
                    break;
            }
            System.out.println();
        }
    }

    private static void addProductInteraction(MyLinkedList<Product> products, Scanner scanner) {
        System.out.print("Enter product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        products.insert(new Product(id, name, price, stock));
        System.out.println("Product added.");
    }

    private static void addCustomerInteraction(MyLinkedList<Customer> customers, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        customers.insert(new Customer(id, name, email));
        System.out.println("Customer added.");
    }

    private static void placeOrderInteraction(MyLinkedList<Order> orders, Scanner scanner, MyLinkedList<Customer> customers, MyLinkedList<Product> products) {
        System.out.print("Enter customer ID: ");
        int cid = scanner.nextInt();
        Customer customer = findCustomerById(customers, cid);
        if (customer == null) {
            System.out.println("Customer not found.");
            scanner.nextLine();
            return;
        }
        System.out.print("Enter number of products: ");
        int num = scanner.nextInt();
        MyLinkedList<Product> orderProducts = new MyLinkedList<>();
        double total = 0;
        for (int i = 0; i < num; i++) {
            System.out.print("Enter product ID: ");
            int pid = scanner.nextInt();
            Product p = findProductById(products, pid);
            if (p != null && !p.isOutOfStock()) {
                orderProducts.insert(p);
                total += p.getPrice();
                p.reduceStock(1);
            } else {
                System.out.println("Product not found or out of stock.");
            }
        }
        scanner.nextLine();
        System.out.print("Enter order date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        int orderId = orders.size() + 1;
        Order order = new Order(orderId, customer, orderProducts, total, date, "pending");
        customer.placeOrder(order);
        orders.insert(order);
        System.out.println("Order placed.");
    }

    private static void addReviewInteraction(MyLinkedList<Product> products, Scanner scanner, MyLinkedList<Customer> customers) {
        System.out.print("Enter customer ID: ");
        int cid = scanner.nextInt();
        System.out.print("Enter product ID: ");
        int pid = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter comment: ");
        String comment = scanner.nextLine();
        Customer customer = findCustomerById(customers, cid);
        Product product = findProductById(products, pid);
        if (customer != null && product != null) {
            Review review = new Review(customer, product, rating, comment);
            product.addReview(review);
            System.out.println("Review added.");
        } else {
            System.out.println("Customer or Product not found.");
        }
    }

    private static void printOrderHistoryInteraction(MyLinkedList<Customer> customers, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int cid = scanner.nextInt();
        Customer customer = findCustomerById(customers, cid);
        if (customer != null) {
            customer.printOrderHistory();
        } else {
            System.out.println("Customer not found.");
        }
        scanner.nextLine();
    }

    private static void printOutOfStock(MyLinkedList<Product> products) {
        System.out.println("Out-of-Stock Products:");
        Node<Product> current = products.head;
        while (current != null) {
            if (current.data.isOutOfStock()) {
                System.out.println(current.data);
            }
            current = current.next;
        }
    }

    private static void printTopRatedProducts(MyLinkedList<Product> products) {
        Product[] top = new Product[3];
        double[] ratings = new double[3];
        Node<Product> current = products.head;
        while (current != null) {
            double avg = current.data.getAverageRating();
            for (int i = 0; i < 3; i++) {
                if (avg > ratings[i]) {
                    for (int j = 2; j > i; j--) {
                        top[j] = top[j - 1];
                        ratings[j] = ratings[j - 1];
                    }
                    top[i] = current.data;
                    ratings[i] = avg;
                    break;
                }
            }
            current = current.next;
        }
        System.out.println("Top 3 Products by Rating:");
        for (int i = 0; i < 3 && top[i] != null; i++) {
            System.out.println(top[i] + " | Avg Rating: " + String.format("%.2f", ratings[i]));
        }
    }

    private static void printOrdersBetweenDates(MyLinkedList<Order> orders, Scanner scanner) {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String start = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String end = scanner.nextLine();
        System.out.println("Orders between " + start + " and " + end + ":");
        Node<Order> current = orders.head;
        while (current != null) {
            if (current.data.isWithinDateRange(start, end)) {
                System.out.println(current.data);
            }
            current = current.next;
        }
    }

    private static void printCommonHighRatedProducts(MyLinkedList<Product> products, Scanner scanner, MyLinkedList<Customer> customers) {
        System.out.print("Enter first customer ID: ");
        int id1 = scanner.nextInt();
        System.out.print("Enter second customer ID: ");
        int id2 = scanner.nextInt();
        scanner.nextLine();
        Customer c1 = findCustomerById(customers, id1);
        Customer c2 = findCustomerById(customers, id2);
        if (c1 == null || c2 == null) {
            System.out.println("One or both customers not found.");
            return;
        }
        MyLinkedList<Product> common = new MyLinkedList<>();
        Node<Product> prod = products.head;
        while (prod != null) {
            boolean reviewedByC1 = false, reviewedByC2 = false;
            Node<Review> rev = prod.data.getReviews().head;
            while (rev != null) {
                if (rev.data.getCustomerId() == id1) reviewedByC1 = true;
                if (rev.data.getCustomerId() == id2) reviewedByC2 = true;
                rev = rev.next;
            }
            if (reviewedByC1 && reviewedByC2 && prod.data.getAverageRating() > 4) {
                common.insert(prod.data);
            }
            prod = prod.next;
        }
        System.out.println("Common High-Rated Products:");
        common.printAll();
    }

    private static void printAllProducts(MyLinkedList<Product> products) {
        System.out.println("All Products:");
        Node<Product> current = products.head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    private static void printAllCustomers(MyLinkedList<Customer> customers) {
        System.out.println("All Customers:");
        Node<Customer> current = customers.head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    private static void printAllOrders(MyLinkedList<Order> orders) {
        System.out.println("All Orders:");
        Node<Order> current = orders.head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    private static void printAllProductsWithRatings(MyLinkedList<Product> products) {
        System.out.println("All Products with Average Ratings:");
        Node<Product> current = products.head;
        while (current != null) {
            Product p = current.data;
            double avgRating = p.getAverageRating();
            System.out.println(p + " | Average Rating: " + String.format("%.2f", avgRating));
            current = current.next;
        }
    }

    private static void editProduct(MyLinkedList<Product> products, Scanner scanner) {
        System.out.print("Enter product ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Node<Product> current = products.head;
        boolean found = false;
        while (current != null) {
            if (current.data.getProductId() == id) {
                found = true;
                System.out.println("Current product: " + current.data);
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                System.out.print("Enter new price: ");
                double price = scanner.nextDouble();
                System.out.print("Enter new stock: ");
                int stock = scanner.nextInt();
                scanner.nextLine();
                Product newProduct = new Product(id, name, price, stock);
                Node<Review> oldReviews = current.data.getReviews().head;
                while (oldReviews != null) {
                    newProduct.addReview(oldReviews.data);
                    oldReviews = oldReviews.next;
                }
                current.data = newProduct;
                System.out.println("Product updated.");
                break;
            }
            current = current.next;
        }
        if (!found) System.out.println("Product not found.");
    }

    private static void removeProduct(MyLinkedList<Product> products, Scanner scanner) {
        System.out.print("Enter product ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Node<Product> current = products.head;
        Node<Product> prev = null;
        boolean found = false;
        while (current != null) {
            if (current.data.getProductId() == id) {
                found = true;
                if (prev == null) {
                    products.head = current.next; 
                } else {
                    prev.next = current.next; 
                }
                System.out.println("Product removed.");
                break;
            }
            prev = current;
            current = current.next;
        }
        if (!found) System.out.println("Product not found.");
    }

    private static void editCustomer(MyLinkedList<Customer> customers, Scanner scanner) {
        System.out.print("Enter customer ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Node<Customer> current = customers.head;
        boolean found = false;
        while (current != null) {
            if (current.data.getCustomerId() == id) {
                found = true;
                System.out.println("Current customer: " + current.data);
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                System.out.print("Enter new email: ");
                String email = scanner.nextLine();
                Customer newCustomer = new Customer(id, name, email);
                Node<Order> oldOrders = current.data.getOrders().head;
                while(oldOrders != null) {
                    newCustomer.placeOrder(oldOrders.data);
                    oldOrders = oldOrders.next;
                }
                current.data = newCustomer;
                System.out.println("Customer updated.");
                break;
            }
            current = current.next;
        }
        if (!found) System.out.println("Customer not found.");
    }

    private static void removeCustomer(MyLinkedList<Customer> customers, Scanner scanner) {
        System.out.print("Enter customer ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Node<Customer> current = customers.head;
        Node<Customer> prev = null;
        boolean found = false;
        while (current != null) {
            if (current.data.getCustomerId() == id) {
                found = true;
                if (prev == null) {
                    customers.head = current.next;
                } else {
                    prev.next = current.next;
                }
                System.out.println("Customer removed.");
                break;
            }
            prev = current;
            current = current.next;
        }
        if (!found) System.out.println("Customer not found.");
    }

    private static void editOrder(MyLinkedList<Order> orders, Scanner scanner, MyLinkedList<Customer> customers, MyLinkedList<Product> products) {
        System.out.print("Enter order ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Node<Order> current = orders.head;
        boolean found = false;
        while (current != null) {
            if (current.data.getOrderId() == id) {
                found = true;
                System.out.println("Current order: " + current.data);
                System.out.print("Enter new status: ");
                String status = scanner.nextLine();
                current.data.updateStatus(status); 
                System.out.println("Order updated.");
                break;
            }
            current = current.next;
        }
        if (!found) System.out.println("Order not found.");
    }

    private static void removeOrder(MyLinkedList<Order> orders, Scanner scanner, MyLinkedList<Customer> customers) {
        System.out.print("Enter order ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Node<Order> current = orders.head;
        Node<Order> prev = null;
        boolean found = false;
        while (current != null) {
            if (current.data.getOrderId() == id) {
                found = true;
                if (prev == null) {
                    orders.head = current.next;
                } else {
                    prev.next = current.next;
                }
                
                Customer c = current.data.getCustomer();
                if (c != null) {
                    Node<Order> cOrder = c.getOrders().head;
                    Node<Order> cPrev = null;
                    while(cOrder != null) {
                        if (cOrder.data.getOrderId() == id) {
                            if(cPrev == null) c.getOrders().head = cOrder.next;
                            else cPrev.next = cOrder.next;
                            break;
                        }
                        cPrev = cOrder;
                        cOrder = cOrder.next;
                    }
                }
                
                System.out.println("Order removed.");
                break;
            }
            prev = current;
            current = current.next;
        }
        if (!found) System.out.println("Order not found.");
    }

    private static void editReview(MyLinkedList<Product> products, Scanner scanner) {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); 

        Product product = findProductById(products, productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        Node<Review> currentReviewNode = product.getReviews().head;
        boolean found = false;
        while (currentReviewNode != null) {
            if (currentReviewNode.data.getCustomerId() == customerId) {
                found = true;
                System.out.println("Current review: " + currentReviewNode.data);

                System.out.print("Enter new rating (1-5): ");
                int newRating = scanner.nextInt();
                scanner.nextLine(); 

                System.out.print("Enter new comment: ");
                String newComment = scanner.nextLine();

                Review newReview = new Review(currentReviewNode.data.getCustomer(), product, newRating, newComment);
                currentReviewNode.data = newReview; 

                System.out.println("Review updated successfully.");
                break;
            }
            currentReviewNode = currentReviewNode.next;
        } 

        if (!found) {
            System.out.println("Review from this customer for this product not found.");
        }
    }

    private static Product findProductById(MyLinkedList<Product> products, int id) {
        Node<Product> current = products.head;
        while (current != null) {
            if (current.data.getProductId() == id) {
                return current.data;
            }
            current = current.next;
        }
        return null; 
    }

    private static Customer findCustomerById(MyLinkedList<Customer> customers, int id) {
        Node<Customer> current = customers.head;
        while (current != null) {
            if (current.data.getCustomerId() == id) {
                return current.data;
            }
            current = current.next;
        }
        return null; 
    }


}
