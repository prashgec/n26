package com.n26.n26task;

import com.n26.constants.Constant;
import com.n26.controller.TransactionController;
import com.n26.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class N26TaskApplicationTests {

	@Autowired
	TransactionController transactionController;

	@Test
	public void testStoreStatistics() {
		Transaction freshTransaction= new Transaction(12.3,System.currentTimeMillis(),0);
		Assert.assertEquals(HttpStatus.CREATED,transactionController.storeTransaction(freshTransaction));
		freshTransaction.setTimestamp(System.currentTimeMillis()+ Constant.MILIS_THRESHOLD);
		Assert.assertEquals(HttpStatus.NO_CONTENT,transactionController.storeTransaction(freshTransaction));
	}

	@Test
	public void testGetStatistics() throws InterruptedException {
		Transaction transaction2 =new Transaction(3.5,System.currentTimeMillis(),0);
		Transaction transaction1= new Transaction(12.3,System.currentTimeMillis()-(10000),0);
		System.out.println(transactionController.storeTransaction(transaction1));
		System.out.println(transactionController.storeTransaction(transaction2));
        Thread.sleep(6);
		Assert.assertEquals(transaction1.getAmount(),transactionController.getStatistics().getMax(),0.1);
		Assert.assertEquals(transaction2.getAmount(),transactionController.getStatistics().getMin(),0.1);
		Assert.assertEquals((transaction1.getAmount()+transaction2.getAmount())/2,transactionController.getStatistics().getAverage(),0.1);
		Assert.assertEquals(2,transactionController.getStatistics().getCount());
		Assert.assertEquals(transaction1.getAmount()+transaction2.getAmount(),transactionController.getStatistics().getSum(),0.1);
		Thread.sleep(50000);
		Assert.assertEquals(transaction2.getAmount(),transactionController.getStatistics().getMax(),0.1);
		Assert.assertEquals(transaction2.getAmount(),transactionController.getStatistics().getMin(),0.1);
		Assert.assertEquals((transaction2.getAmount())/1,transactionController.getStatistics().getAverage(),0.1);
		Assert.assertEquals(1,transactionController.getStatistics().getCount());
		Assert.assertEquals(transaction2.getAmount(),transactionController.getStatistics().getSum(),0.1);

	}

}
