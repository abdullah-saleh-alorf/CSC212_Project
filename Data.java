package csc212.copy;

import java.io.BufferedReader;
import java.io.FileReader;

public class Data {

    public static void loadProducts(AVLTree<Product> products) {
        loadFile("products.csv", (parts) -> {
            try {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                int stock = Integer.parseInt(parts[3].trim());

                products.insert(id, new Product(id, name, price, stock));
            } catch (Exception e) {
                System.out.println("تجاوز خطأ في سطر منتج: " + String.join(",", parts));
            }
        });

        System.out.println("تم تحميل المنتجات: " + products.size());
    }

    public static void loadCustomers(AVLTree<Customer> customers) {
        loadFile("customers.csv", (parts) -> {
            try {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String email = parts[2].trim();

                customers.insert(id, new Customer(id, name, email));
            } catch (Exception e) {
                System.out.println("تجاوز خطأ في سطر عميل: " + String.join(",", parts));
            }
        });
        System.out.println("تم تحميل العملاء: " + customers.size());
    }

    public static void loadOrders(AVLTree<Order> orders,
                                  AVLTree<Customer> customers,
                                  AVLTree<Product> products) {

        loadFile("orders.csv", (parts) -> {
            try {
                int orderId = Integer.parseInt(parts[0].trim());
                int customerId = Integer.parseInt(parts[1].trim());
                String[] productIds = parts[2].split(";");
                double totalPrice = Double.parseDouble(parts[3].trim());
                String date = parts[4].trim();
                String status = parts[5].trim();

                Customer customer = customers.search(customerId);
                if (customer == null) return;

                MyLinkedList<Product> orderProducts = new MyLinkedList<>();

                for (String pid : productIds) {
                    int pidInt = Integer.parseInt(pid.trim());
                    Product p = products.search(pidInt);
                    if (p != null) orderProducts.insert(p);
                }

                Order order = new Order(orderId, customer, orderProducts, totalPrice, date, status);

                customer.getOrders().insert(order);
                orders.insert(orderId, order);

            } catch (Exception e) {
                System.out.println("تجاوز خطأ في سطر طلب: " + String.join(",", parts));
            }
        });

        System.out.println("تم تحميل الطلبات: " + orders.size());
    }

    public static void loadReviews(AVLTree<Product> products,
                                   AVLTree<Customer> customers) {

        loadFile("reviews.csv", (parts) -> {
            try {
                int reviewId = Integer.parseInt(parts[0].trim());
                int productId = Integer.parseInt(parts[1].trim());
                int customerId = Integer.parseInt(parts[2].trim());
                int rating = Integer.parseInt(parts[3].trim());
                String comment = parts[4].trim();

                Product product = products.search(productId);
                Customer customer = customers.search(customerId);

                if (product != null && customer != null) {
                    Review review = new Review(customer, product, rating, comment);
                    product.addReview(review);
                }
            } catch (Exception e) {
                System.out.println("تجاوز خطأ في سطر تقييم: " + String.join(",", parts));
            }
        });

        System.out.println("تم تحميل التقييمات.");
    }

    private static void loadFile(String fileName, LineProcessor processor) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                processor.process(line.split(","));
            }

        } catch (Exception e) {
            System.out.println("تعذّر تحميل الملف: " + fileName);
            e.printStackTrace();
        }
    }

    interface LineProcessor {
        void process(String[] parts);
    }
}
