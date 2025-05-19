package com.fullstackbackend;

import com.fullstackbackend.DAO.AccountRepository;
import com.fullstackbackend.controller.AccountController;
import com.fullstackbackend.exception.NotFoundException;
import com.fullstackbackend.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebMvcTest(AccountController.class)
class AccountControllerTest {
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountController controller;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testTransaction_SuccessfulTransfer() {
		Integer idFortran = 2; // аккаунт получателя
		Integer idSender = 1; // аккаунт отправителя
		BigDecimal transferAmount = new BigDecimal("100");

		Account senderAccount = new Account();
		senderAccount.setId(idSender);
		senderAccount.setBalance(new BigDecimal("500"));

		Account receiverAccount = new Account();
		receiverAccount.setId(idFortran);
		receiverAccount.setBalance(new BigDecimal("200"));

		when(accountRepository.findByUser(idSender))
				.thenReturn(Optional.of(senderAccount));
		when(accountRepository.findByUser(idFortran))
				.thenReturn(Optional.of(receiverAccount));

		controller.transaction(idFortran, transferAmount, idSender);

		assertEquals(new BigDecimal("400"), senderAccount.getBalance());
		assertEquals(new BigDecimal("300"), receiverAccount.getBalance());

		verify(accountRepository).save(senderAccount);
		verify(accountRepository).save(receiverAccount);
	}

	@Test
	public void testTransaction_InsufficientBalance() {
		Integer idFortran = 2;
		Integer idSender = 1;
		BigDecimal transferAmount = new BigDecimal("600");

		Account senderAccount = new Account();
		senderAccount.setId(idSender);
		senderAccount.setBalance(new BigDecimal("500"));

		when(accountRepository.findByUser(idSender))
				.thenReturn(Optional.of(senderAccount));

		controller.transaction(idFortran, transferAmount, idSender);
		assertEquals(new BigDecimal("500"), senderAccount.getBalance());
		verify(accountRepository).save(any());
	}

	@Test
	public void testTransaction_ReceiverNotFound() {
		Integer idFortran = 2;
		Integer idSender = 1;
		BigDecimal transferAmount = new BigDecimal("100");

		Account senderAccount = new Account();
		senderAccount.setId(idSender);
		senderAccount.setBalance(new BigDecimal("500"));

		when(accountRepository.findByUser(idSender))
				.thenReturn(Optional.of(senderAccount));

		when(accountRepository.findByUser(idFortran))
				.thenReturn(Optional.empty()); // аккаунт получателя не найден

		assertThrows(NotFoundException.class, () -> {
			controller.transaction(idFortran, transferAmount, idSender);
		});

		verify(accountRepository, never()).save(any());
	}
}
