CREATE TABLE order_item(
  ORDER_ID     INT,
  PRODUCT_ID   INT,
  QUANTITY     FLOAT,
  PRICE        FLOAT,
  PRIMARY KEY (ORDER_ID, PRODUCT_ID),
  CONSTRAINT ORDER_FK FOREIGN KEY (ORDER_ID) REFERENCES "order" (id),
  CONSTRAINT PRODUCT_FK FOREIGN KEY (PRODUCT_ID) REFERENCES product (id)
);