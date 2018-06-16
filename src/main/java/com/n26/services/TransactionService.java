package com.n26.services;

import com.n26.cache.LocalCache;
import com.n26.constants.Constant;
import com.n26.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Component
public class TransactionService {

    LocalCache<Long, List<Transaction>> cache= new LocalCache<Long, List<Transaction>>();


    public boolean storeTransaction(Transaction transaction)
    {
        if(System.currentTimeMillis()-transaction.getTimestamp()> Constant.MILIS_THRESHOLD || System.currentTimeMillis()-transaction.getTimestamp()<0 )
            return false;
        else
        {
            transaction.setExpiryTimestamp(transaction.getTimestamp()+Constant.MILIS_THRESHOLD);
            if(cache.get(transaction.getExpiryTimestamp())==null)
            {
                List<Transaction> txns= new ArrayList<>();
                txns.add(transaction);
                cache.put(transaction.getExpiryTimestamp(),txns);}
                else{
                List<Transaction> txns=cache.get(transaction.getExpiryTimestamp());
                cache.put(transaction.getExpiryTimestamp(), txns);
            }
            return true;
        }



    }

    public DoubleSummaryStatistics getStatistics()
    {
        return cache.getStatistics();

    }
}
