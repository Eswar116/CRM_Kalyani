package com.crm.crmapp.order.model;

import java.io.Serializable;

public class Pagination implements Serializable {
    public String pageNumber;
    public String total_no_records;
    public String maximumPages;
    public String pageSize;
}