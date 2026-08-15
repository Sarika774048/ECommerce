package com.infinitycart.service;

import com.infinitycart.model.Seller;
import com.infinitycart.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);

}
