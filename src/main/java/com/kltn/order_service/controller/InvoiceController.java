package com.kltn.order_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kltn.order_service.dto.response.ResponseData;
import com.kltn.order_service.dto.response.ResponseError;
import com.kltn.order_service.service.InvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/")
    public ResponseData<?> getMethodName() {
        try {
            String message = invoiceService.generateInvoice();
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Generate order invoice successful!", message);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
