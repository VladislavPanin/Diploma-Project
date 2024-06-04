\c db_diplom

ALTER TABLE order_numbers_dictionary ADD CONSTRAINT dictionary_to_region_id_main_fk     FOREIGN KEY(region_id)   REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary ADD CONSTRAINT dictionary_to_market_id_main_fk     FOREIGN KEY(market_id)   REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary ADD CONSTRAINT dictionary_to_terminal_id_main_fk   FOREIGN KEY(terminal_id) REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary ADD CONSTRAINT dictionary_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);

ALTER TABLE sales ADD CONSTRAINT sales_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary (order_number);
ALTER TABLE sales ADD CONSTRAINT sales_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales ADD CONSTRAINT sales_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales ADD CONSTRAINT sales_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales ADD CONSTRAINT sales_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales ADD CONSTRAINT sales_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales ADD CONSTRAINT sales_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales ADD CONSTRAINT sales_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary (region_id, market_id, terminal_id, order_number);

ALTER TABLE order_numbers_dictionary__01 ADD CONSTRAINT dictionary__01_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__01 ADD CONSTRAINT dictionary__01_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__01 ADD CONSTRAINT dictionary__01_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__01 ADD CONSTRAINT dictionary__01_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__02 ADD CONSTRAINT dictionary__02_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__02 ADD CONSTRAINT dictionary__02_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__02 ADD CONSTRAINT dictionary__02_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__02 ADD CONSTRAINT dictionary__02_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__03 ADD CONSTRAINT dictionary__03_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__03 ADD CONSTRAINT dictionary__03_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__03 ADD CONSTRAINT dictionary__03_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__03 ADD CONSTRAINT dictionary__03_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__04 ADD CONSTRAINT dictionary__04_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__04 ADD CONSTRAINT dictionary__04_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__04 ADD CONSTRAINT dictionary__04_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__04 ADD CONSTRAINT dictionary__04_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__05 ADD CONSTRAINT dictionary__05_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__05 ADD CONSTRAINT dictionary__05_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__05 ADD CONSTRAINT dictionary__05_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__05 ADD CONSTRAINT dictionary__05_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__06 ADD CONSTRAINT dictionary__06_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__06 ADD CONSTRAINT dictionary__06_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__06 ADD CONSTRAINT dictionary__06_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__06 ADD CONSTRAINT dictionary__06_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__07 ADD CONSTRAINT dictionary__07_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__07 ADD CONSTRAINT dictionary__07_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__07 ADD CONSTRAINT dictionary__07_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__07 ADD CONSTRAINT dictionary__07_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__08 ADD CONSTRAINT dictionary__08_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__08 ADD CONSTRAINT dictionary__08_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__08 ADD CONSTRAINT dictionary__08_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__08 ADD CONSTRAINT dictionary__08_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                    
ALTER TABLE order_numbers_dictionary__09 ADD CONSTRAINT dictionary__09_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__09 ADD CONSTRAINT dictionary__09_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__09 ADD CONSTRAINT dictionary__09_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__09 ADD CONSTRAINT dictionary__09_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);

ALTER TABLE order_numbers_dictionary__10 ADD CONSTRAINT dictionary__10_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__10 ADD CONSTRAINT dictionary__10_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__10 ADD CONSTRAINT dictionary__10_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__10 ADD CONSTRAINT dictionary__10_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__20 ADD CONSTRAINT dictionary__20_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__20 ADD CONSTRAINT dictionary__20_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__20 ADD CONSTRAINT dictionary__20_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__20 ADD CONSTRAINT dictionary__20_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__30 ADD CONSTRAINT dictionary__30_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__30 ADD CONSTRAINT dictionary__30_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__30 ADD CONSTRAINT dictionary__30_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__30 ADD CONSTRAINT dictionary__30_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__40 ADD CONSTRAINT dictionary__40_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__40 ADD CONSTRAINT dictionary__40_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__40 ADD CONSTRAINT dictionary__40_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__40 ADD CONSTRAINT dictionary__40_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__50 ADD CONSTRAINT dictionary__50_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__50 ADD CONSTRAINT dictionary__50_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__50 ADD CONSTRAINT dictionary__50_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__50 ADD CONSTRAINT dictionary__50_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__60 ADD CONSTRAINT dictionary__60_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__60 ADD CONSTRAINT dictionary__60_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__60 ADD CONSTRAINT dictionary__60_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__60 ADD CONSTRAINT dictionary__60_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__70 ADD CONSTRAINT dictionary__70_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__70 ADD CONSTRAINT dictionary__70_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__70 ADD CONSTRAINT dictionary__70_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__70 ADD CONSTRAINT dictionary__70_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__80 ADD CONSTRAINT dictionary__80_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__80 ADD CONSTRAINT dictionary__80_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__80 ADD CONSTRAINT dictionary__80_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__80 ADD CONSTRAINT dictionary__80_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary__90 ADD CONSTRAINT dictionary__90_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary__90 ADD CONSTRAINT dictionary__90_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary__90 ADD CONSTRAINT dictionary__90_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary__90 ADD CONSTRAINT dictionary__90_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);
                                                                  
ALTER TABLE order_numbers_dictionary_100 ADD CONSTRAINT dictionary_100_to_region_id_main_fk    FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE order_numbers_dictionary_100 ADD CONSTRAINT dictionary_100_to_market_id_main_fk    FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE order_numbers_dictionary_100 ADD CONSTRAINT dictionary_100_to_terminal_id_main_fk  FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE order_numbers_dictionary_100 ADD CONSTRAINT dictionary_100_main_uniq UNIQUE (region_id, market_id, terminal_id, order_number);


ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__01 (order_number);
ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__01 ADD CONSTRAINT sales__01_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__01 (region_id, market_id, terminal_id, order_number);
                                            
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__02 (order_number);
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__02 ADD CONSTRAINT sales__02_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__02 (region_id, market_id, terminal_id, order_number);
                                            
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__03 (order_number);
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__03 ADD CONSTRAINT sales__03_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__03 (region_id, market_id, terminal_id, order_number);
                                          
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__04 (order_number);
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__04 ADD CONSTRAINT sales__04_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__04 (region_id, market_id, terminal_id, order_number);
                                            
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__05 (order_number);
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__05 ADD CONSTRAINT sales__05_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__05 (region_id, market_id, terminal_id, order_number);
                                            
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__06 (order_number);
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__06 ADD CONSTRAINT sales__06_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__06 (region_id, market_id, terminal_id, order_number);
                                              
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__07 (order_number);
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__07 ADD CONSTRAINT sales__07_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__07 (region_id, market_id, terminal_id, order_number);
                                              
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__08 (order_number);
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__08 ADD CONSTRAINT sales__08_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__08 (region_id, market_id, terminal_id, order_number);
                                              
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__09 (order_number);
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__09 ADD CONSTRAINT sales__09_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__09 (region_id, market_id, terminal_id, order_number);

ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__10 (order_number);
ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__10 ADD CONSTRAINT sales__10_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__10 (region_id, market_id, terminal_id, order_number);
                                          
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__20 (order_number);
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__20 ADD CONSTRAINT sales__20_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__20 (region_id, market_id, terminal_id, order_number);
                                          
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__30 (order_number);
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__30 ADD CONSTRAINT sales__30_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__30 (region_id, market_id, terminal_id, order_number);
                                          
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__40 (order_number);
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__40 ADD CONSTRAINT sales__40_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__40 (region_id, market_id, terminal_id, order_number);
                                          
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__50 (order_number);
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__50 ADD CONSTRAINT sales__50_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__50 (region_id, market_id, terminal_id, order_number);

ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__60 (order_number);
ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__60 ADD CONSTRAINT sales__60_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__60 (region_id, market_id, terminal_id, order_number);
                                              
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__70 (order_number);
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__70 ADD CONSTRAINT sales__70_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__70 (region_id, market_id, terminal_id, order_number);
                                              
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__80 (order_number);
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__80 ADD CONSTRAINT sales__80_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__80 (region_id, market_id, terminal_id, order_number);
                                              
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary__90 (order_number);
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales__90 ADD CONSTRAINT sales__90_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary__90 (region_id, market_id, terminal_id, order_number);
                                              
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_order_number_main_fk  FOREIGN KEY(order_number) REFERENCES order_numbers_dictionary_100 (order_number);
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_region_id_main_fk     FOREIGN KEY(region_id)    REFERENCES regions (id);
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_market_id_main_fk     FOREIGN KEY(market_id)    REFERENCES markets (id);
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_terminal_id_main_fk   FOREIGN KEY(terminal_id)  REFERENCES terminals (id);
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_product_id_main_fk    FOREIGN KEY(product_id)   REFERENCES products (id);
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_client_id_main_fk     FOREIGN KEY(client_id)    REFERENCES clients (id);
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_terminals_main_fk     FOREIGN KEY(region_id, market_id, terminal_id)               REFERENCES terminals (region_id, market_id, terminal_id_on_market);
ALTER TABLE sales_100 ADD CONSTRAINT sales_100_to_dictionary_main_fk    FOREIGN KEY(region_id, market_id, terminal_id, order_number) REFERENCES order_numbers_dictionary_100 (region_id, market_id, terminal_id, order_number);

