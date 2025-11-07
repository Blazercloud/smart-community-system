create table admin
(
    id          int auto_increment
        primary key,
    username    varchar(50)                           not null,
    password    varchar(100)                          not null,
    role        varchar(20) default 'ADMIN'           null,
    create_time timestamp   default CURRENT_TIMESTAMP null,
    status      int                                   null,
    constraint username
        unique (username)
)
    row_format = DYNAMIC;

create table notice
(
    id           bigint auto_increment comment '公告ID'
        primary key,
    title        varchar(200)                       not null comment '标题',
    content      text                               not null comment '内容',
    type         tinyint  default 1                 null comment '类型：1-社区公告，2-物业通知',
    publisher_id bigint                             null comment '发布人ID',
    status       tinyint  default 1                 null comment '状态：0-草稿，1-已发布，2-已过期',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '公告表' charset = utf8mb4
                     row_format = DYNAMIC;

create index idx_status
    on notice (status);

create index idx_type
    on notice (type);

create table shop
(
    id             int auto_increment
        primary key,
    name           varchar(100)                        not null,
    owner_name     varchar(50)                         null,
    username       varchar(50)                         not null,
    password       varchar(100)                        not null,
    phone          varchar(20)                         null,
    description    text                                null,
    create_time    timestamp default CURRENT_TIMESTAMP null,
    address        varchar(200)                        null comment '店铺地址',
    business_hours varchar(100)                        null comment '营业时间',
    logo           varchar(200)                        null comment '店铺logo',
    status         tinyint   default 1                 null comment '状态：0-停业，1-营业',
    constraint shop_unique_username
        unique (username),
    constraint username
        unique (username)
)
    row_format = DYNAMIC;

create table product
(
    id          int auto_increment
        primary key,
    shop_id     int                                   not null comment '商家ID',
    name        varchar(200)                          not null comment '商品名称',
    description text                                  null comment '商品描述',
    price       decimal(10, 2)                        not null comment '商品价格',
    stock       int         default 0                 not null comment '库存数量',
    category    varchar(50) default '其他'            null comment '商品分类',
    images      text                                  null comment '商品图片',
    status      tinyint     default 1                 null comment '状态：0-下架，1-上架',
    create_time timestamp   default CURRENT_TIMESTAMP null,
    update_time timestamp   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint fk_product_shop
        foreign key (shop_id) references shop (id)
            on delete cascade
)
    charset = utf8mb4;

create index idx_category
    on product (category);

create index idx_shop_id
    on product (shop_id);

create index idx_status
    on product (status);

create table user
(
    id          int auto_increment
        primary key,
    username    varchar(50)                         not null,
    password    varchar(100)                        not null,
    phone       varchar(20)                         null,
    email       varchar(100)                        null,
    room_number varchar(20)                         null,
    house_type  varchar(50)                         null,
    create_time timestamp default CURRENT_TIMESTAMP null,
    status      int                                 null,
    constraint username
        unique (username)
)
    row_format = DYNAMIC;

create table cart
(
    id          int auto_increment
        primary key,
    user_id     int                                 not null comment '用户ID',
    product_id  int                                 not null comment '商品ID',
    quantity    int       default 1                 not null comment '购买数量',
    create_time timestamp default CURRENT_TIMESTAMP null,
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint uk_user_product
        unique (user_id, product_id),
    constraint fk_cart_product
        foreign key (product_id) references product (id)
            on delete cascade,
    constraint fk_cart_user
        foreign key (user_id) references user (id)
            on delete cascade
)
    charset = utf8mb4;

create index idx_user_id
    on cart (user_id);

create table complaint
(
    id            int auto_increment
        primary key,
    user_id       int                                 not null,
    content       text                                not null,
    is_handled    tinyint   default 0                 null,
    handle_result text                                null,
    create_time   timestamp default CURRENT_TIMESTAMP null,
    constraint complaint_ibfk_1
        foreign key (user_id) references user (id)
)
    row_format = DYNAMIC;

create index user_id
    on complaint (user_id);

create table orders
(
    id               int auto_increment
        primary key,
    order_no         varchar(50)                           not null comment '订单号',
    user_id          int                                   not null comment '用户ID',
    shop_id          int                                   not null comment '商家ID',
    total_amount     decimal(10, 2)                        not null comment '订单总金额',
    status           varchar(20) default 'pending'         null comment '订单状态：pending-待付款, paid-已付款, shipped-已发货, completed-已完成, cancelled-已取消',
    payment_method   varchar(20)                           null comment '支付方式',
    payment_time     datetime                              null comment '支付时间',
    shipping_address text                                  null comment '收货地址',
    receiver_name    varchar(50)                           null comment '收货人姓名',
    receiver_phone   varchar(20)                           null comment '收货人电话',
    shipping_time    datetime                              null comment '发货时间',
    tracking_no      varchar(50)                           null comment '快递单号',
    complete_time    datetime                              null comment '完成时间',
    create_time      timestamp   default CURRENT_TIMESTAMP null,
    update_time      timestamp   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint uk_order_no
        unique (order_no),
    constraint fk_order_shop
        foreign key (shop_id) references shop (id),
    constraint fk_order_user
        foreign key (user_id) references user (id)
)
    charset = utf8mb4;

create table order_item
(
    id            int auto_increment
        primary key,
    order_id      int            not null comment '订单ID',
    product_id    int            not null comment '商品ID',
    product_name  varchar(200)   not null comment '商品名称',
    product_price decimal(10, 2) not null comment '商品单价',
    quantity      int            not null comment '购买数量',
    total_price   decimal(10, 2) not null comment '小计金额',
    constraint fk_order_item_order
        foreign key (order_id) references orders (id)
            on delete cascade,
    constraint fk_order_item_product
        foreign key (product_id) references product (id)
)
    charset = utf8mb4;

create index idx_order_id
    on order_item (order_id);

create index idx_product_id
    on order_item (product_id);

create index idx_create_time
    on orders (create_time);

create index idx_shop_id
    on orders (shop_id);

create index idx_status
    on orders (status);

create index idx_user_id
    on orders (user_id);

create table parking_application
(
    id           int auto_increment
        primary key,
    user_id      int                                   not null comment '用户ID',
    space_number varchar(20)                           null comment '车位号',
    car_number   varchar(20)                           null comment '车牌号',
    status       varchar(20) default '0'               null comment '0-待审批，1-已同意，2-已退回',
    apply_time   timestamp   default CURRENT_TIMESTAMP null,
    constraint parking_application_ibfk_1
        foreign key (user_id) references user (id)
)
    row_format = DYNAMIC;

create index user_id
    on parking_application (user_id);

create table parking_space
(
    id           int auto_increment
        primary key,
    space_number varchar(20)                not null,
    status       varchar(20) default '空闲' null comment '0-空闲，1-已占用',
    owner_id     int                        null,
    car_number   varchar(20)                null,
    constraint space_number
        unique (space_number),
    constraint parking_space_ibfk_1
        foreign key (owner_id) references user (id)
)
    row_format = DYNAMIC;

create index owner_id
    on parking_space (owner_id);

create table property_payment
(
    id          int auto_increment comment '缴费订单ID'
        primary key,
    user_id     int                                   not null comment '用户ID',
    amount      decimal(10, 2)                        not null comment '缴费金额',
    type        varchar(20)                           not null comment '费用类型：property/parking/other',
    status      varchar(20) default 'pending'         not null comment '支付状态：pending/paid/refunded',
    pay_method  varchar(20)                           null comment '支付方式：wallet/alipay/wechat',
    note        varchar(255)                          null comment '备注',
    create_time datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint fk_property_payment_user
        foreign key (user_id) references user (id)
)
    comment '物业缴费订单表' charset = utf8mb4;

create table repair
(
    id              int auto_increment
        primary key,
    user_id         int                                 not null,
    repair_type     varchar(50)                         not null comment '报修类型',
    title           varchar(100)                        not null comment '报修标题',
    description     text                                not null comment '详细描述',
    img_url         varchar(1000)                       null comment '图片URL',
    phone           varchar(20)                         not null comment '联系电话',
    expected_time   datetime                            null comment '期望维修时间',
    status          tinyint   default 0                 null comment '报修状态：0-未审核，1-已审核，2-已处理',
    create_time     timestamp default CURRENT_TIMESTAMP null,
    update_time     timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    assigned_worker varchar(50)                         null comment '指派维修人员',
    handle_detail   text                                null comment '处理详情',
    constraint repair_ibfk_1
        foreign key (user_id) references user (id)
)
    row_format = DYNAMIC;

create index idx_create_time
    on repair (create_time);

create index idx_status
    on repair (status);

create index idx_user_id
    on repair (user_id);

create table visitor
(
    id           int auto_increment
        primary key,
    name         varchar(50)  null,
    visitor_type varchar(50)  null,
    purpose      varchar(100) null,
    visit_time   datetime     null,
    related_user int          null,
    status       int          not null,
    constraint visitor_ibfk_1
        foreign key (related_user) references user (id)
)
    row_format = DYNAMIC;

create index related_user
    on visitor (related_user);

