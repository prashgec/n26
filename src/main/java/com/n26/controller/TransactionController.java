package com.n26.controller;

import com.n26.model.Transaction;
import com.n26.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.DoubleSummaryStatistics;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    /**
     * storing valid transactions
     * @param transaction
     * @return
     */
    @PostMapping("/transactions")
    public HttpStatus storeTransaction(@RequestBody Transaction transaction)
    {
       if(service.storeTransaction(transaction))
        return HttpStatus.CREATED;
       else
           return HttpStatus.NO_CONTENT;
    }

    /**
     * returing the statistics all the in memory transaction from cache
     * @return
     */
    @GetMapping("/statistics")
    public DoubleSummaryStatistics getStatistics()
    {
        return service.getStatistics();
    }
}
