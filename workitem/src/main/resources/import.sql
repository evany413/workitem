

INSERT INTO geps.pcc_developer (pk_pcc_developer,name,email,create_date,update_date) VALUES (pcc_developer_SEQ.nextval,'evan','evan@webcomm.com.tw',sysdate,sysdate ); 
INSERT INTO geps.pcc_developer (pk_pcc_developer,name,email,create_date,update_date) VALUES (pcc_developer_SEQ.nextval,'mike.chen','mike.chen@webcomm.com.tw',sysdate,sysdate ); 
INSERT INTO geps.category (pk_category,description,create_date,update_date) VALUES (category_SEQ.nextval,'休假事項',sysdate,sysdate );
INSERT INTO geps.category (pk_category,description,create_date,update_date) VALUES (category_SEQ.nextval,'待辦事項',sysdate,sysdate ); 
INSERT INTO geps.category (pk_category,description,create_date,update_date) VALUES (category_SEQ.nextval,'三代會議',sysdate,sysdate ); 
INSERT INTO geps.category (pk_category,description,create_date,update_date) VALUES (category_SEQ.nextval,'申訴系統',sysdate,sysdate ); 
INSERT INTO geps.item (pk_item,fk_category,fk_pcc_developer,content,work_time,create_date,update_date) VALUES (item_SEQ.nextval,1,1,'測試代辦事項',8,to_date('2018-10-01','yyyy-mm-dd'),to_date('2018-10-01','yyyy-mm-dd') ); 
INSERT INTO geps.item (pk_item,fk_category,fk_pcc_developer,content,work_time,create_date,update_date) VALUES (item_SEQ.nextval,51,1,'測試申訴系統',4,to_date('2018-10-02','yyyy-mm-dd'),to_date('2018-10-02','yyyy-mm-dd') ); 
INSERT INTO geps.item (pk_item,fk_category,fk_pcc_developer,content,work_time,create_date,update_date) VALUES (item_SEQ.nextval,51,1,'測試申訴系統2',4,to_date('2018-10-02','yyyy-mm-dd'),to_date('2018-10-02','yyyy-mm-dd') ); 
INSERT INTO geps.item (pk_item,fk_category,fk_pcc_developer,content,work_time,create_date,update_date) VALUES (item_SEQ.nextval,101,1,'測試三代會議',8,to_date('2018-10-03','yyyy-mm-dd'),to_date('2018-10-03','yyyy-mm-dd') );