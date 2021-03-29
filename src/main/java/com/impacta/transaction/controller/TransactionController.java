package com.impacta.transaction.controller;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.impacta.transaction.model.Balance;
import com.impacta.transaction.model.Extrato;
import com.impacta.transaction.model.TransactionDTO;
import com.impacta.transaction.model.TransactionDao;
import com.impacta.transaction.model.TransferDTO;
import com.impacta.transaction.service.TransactionDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Controller
@RestController
@RequestMapping("/api/transaction")
public class TransactionController extends AbstractController {

	public TransactionController(Environment e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private TransactionDetailsService movimentacaoDetailsService;

	@Autowired
	JwtAccessTokenConverter convert;

	private static final Logger LOGGER = Logger.getLogger(TransactionController.class);

	private static final String FEEDBACK_MESSAGE_TRANSACTION_CREATED_SUCCESS = "feedback.message.transaction.created.success";
	private static final String ERROR_MESSAGE_TRANSACTION_NOT_CREATED = "error.message.transaction.not.created";
	private static final String FEEDBACK_MESSAGE_BALANCE_AVAILABLE = "feedback.message.balance.available";
	private static final String ERROR_MESSAGE_BALANCE_NOT_AVAILABLE = "error.message.balance.not.available";
	private static final String FEEDBACK_MESSAGE_ALL_TRANSACTIONS_AVAILABLE = "feedback.message.all.transactions.available";
	private static final String ERROR_MESSAGE_ALL_TRANSACTIONS_NOT_AVAILABLE = "error.message.all.transactions.not.available";

	private static final String ERROR_MESSAGE_DEPOSIT_VALUE_NOT_VALID = "error.message.deposit.value.not.valid";
	private static final String ERROR_MESSAGE_DEPOSIT_NOT_AVAILABLE = "error.message.deposit.not.available";
	private static final String FEEDBACK_MESSAGE_DEPOSIT_CREATED = "feedback.message.deposit.was.created";

	private static final String ERROR_MESSAGE_WITHDRAWAL_UNAVAILABLE = "error.message.withdrawal.not.available";
	private static final String ERROR_MESSAGE_WITHDRAWAL_VALUE_NOT_VALID = "error.message.withdrawal.value.not.valid";
	private static final String FEEDBACK_MESSAGE_WITHDRAWAL_CREATED = "feedback.message.withdrawal.was.created";
	private static final String ERROR_MESSAGE_TRANSFER_VALUE_NOT_VALID = "error.message.transfer.value.not.valid";

	private static final String ERROR_MESSAGE_NO_MONEY_TO_THIS_TRANSACTION = "error.message.no.money.to.this.transaction";
	private static final String ERROR_MESSAGE_WRONG_BALANCE_AFTER_TRANSACTION = "error.message.wrong.balance.after.transaction";
	private static final String ERROR_MESSAGE_TRANSFER_UNAVAILABLE = "error.message.transfer.not.available";

	private static final String FEEDBACK_MESSAGE_TRANSFER_CREATED_SUCCESS = "feedback.message.transfer.was.created";

	private static final String ERROR_MESSAGE_ACCOUNT_NUMBER_NOT_FOUND = "error.message.account.not.found";

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Principal> get(final Principal principal) {
		return ResponseEntity.ok(principal);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "api/teste", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getExtrato(final Principal principal) throws Exception {
		return "hello";
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/addTransaction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveMovimetacao(@RequestBody TransactionDTO transacation) throws Exception {
		try {
			if (transacation.getGuidId() == null) {
				UUID uuidTo = UUID.randomUUID();
				transacation.setGuidId(uuidTo.toString());
				System.out.println("UUID=" + uuidTo.toString());
			}
			movimentacaoDetailsService.addTransaction(transacation);
			return new ResponseEntity<>(addFeedbackMessage(FEEDBACK_MESSAGE_TRANSACTION_CREATED_SUCCESS),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(addFeedbackMessage(ERROR_MESSAGE_TRANSACTION_NOT_CREATED), HttpStatus.CREATED);

		}
	}

	public String getCpfFromToken(String token)
			throws NoSuchAlgorithmException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
			SignatureException, IllegalArgumentException, InvalidKeySpecException {
		String key = convert.getKey().get("value");
		key = key.replaceAll("-----BEGIN PUBLIC KEY-----", "");
		key = key.replaceAll("-----END PUBLIC KEY-----", "");
		key = key.replace(" ", "");
		key = key.replace("\n", "");
		key = key.trim();
		System.out.println("chave goottee=" + key + "hello");

		byte[] decode = Base64.getDecoder().decode(key);
		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(decode);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		System.out.println("user_name = " + Jwts.parser().setSigningKey(keyFactory.generatePublic(keySpecX509))
				.parseClaimsJws(token).getBody().get("user_name"));
		return (String) Jwts.parser().setSigningKey(keyFactory.generatePublic(keySpecX509)).parseClaimsJws(token)
				.getBody().get("user_name");
	}

	public int returnAccount(String cpf, int type) {
		if (cpf.equals("37442789821") && type == 1) {
			return 1;
		}
		if (cpf.equals("37442789821") && type == 2) {
			return 2;
		}
		if (cpf.equals("37442789822") && type == 1) {
			return 3;
		}
		if (cpf.equals("37442789822") && type == 2) {
			return 4;
		}
		if (cpf.equals("37442789823") && type == 1) {
			return 5;
		}
		if (cpf.equals("37442789823") && type == 2) {
			return 6;
		}
		return 0;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/balance", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSaldo(@RequestBody TransactionDTO transacation,
			@RequestHeader("Authorization") String auth) throws Exception {
		try {

			String token = auth.replaceAll("Bearer ", "");

			String cpf = getCpfFromToken(token);

			int contaCorrenteIdUser = returnAccount(cpf, 1);
			int contaPoupancaIdUser = returnAccount(cpf, 2);

			LOGGER.info("Starting balance api");
			
			LOGGER.info("Gettting balance api account - Conta corrente");
			LOGGER.info("Getting balance api for account "+contaCorrenteIdUser);
			double saldoAtualContaCorrente = movimentacaoDetailsService.getBalance(contaCorrenteIdUser, 1);;

			LOGGER.info("Balance for account "+contaCorrenteIdUser+" is " + saldoAtualContaCorrente);

			Balance saldoContaCorrente = new Balance(saldoAtualContaCorrente, "Conta Corrente", "BRL", "R$");
			LOGGER.info("Getting balance api account - Poupança");
			LOGGER.info("Getting balance api for account "+contaPoupancaIdUser);
			
			double saldoAtualContaPoupanca = movimentacaoDetailsService.getBalance(contaPoupancaIdUser, 1);
			LOGGER.info("Balance for account "+contaPoupancaIdUser+" is " + saldoAtualContaPoupanca);
			 
			Balance saldoContaPoupanca = new Balance(saldoAtualContaPoupanca, "Conta Poupança", "BRL", "R$");

			List<Balance> listaSaldo = new ArrayList<Balance>();
			listaSaldo.add(saldoContaCorrente);
			listaSaldo.add(saldoContaPoupanca);
			 
			return new ResponseEntity<>(addFeedbackMessage(FEEDBACK_MESSAGE_BALANCE_AVAILABLE, listaSaldo),
					HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("error " + e);
			e.printStackTrace();
			return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_BALANCE_NOT_AVAILABLE), HttpStatus.CREATED);
		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping(value = "/deposit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createDeposit(@RequestBody TransactionDTO transacation) throws Exception {

		try {

			if (transacation.getValue() <= 0) {
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_DEPOSIT_VALUE_NOT_VALID),
						HttpStatus.INTERNAL_SERVER_ERROR);

			}
			LOGGER.info("Starting deposit api");

			LOGGER.info("Value of transaction is " + transacation.getValue());
			LOGGER.info("Account of transaction is " + transacation.getAccountId());

			transacation.setDescriptionStatus("Approved");
			transacation.setStatusTransaction(1);
			transacation.setType_operation("Credit");
			transacation.setMoney_name("BRL");
			transacation.setTypeTransactionId(2);
			transacation.setDescription_type_transaction("Enter - Deposit");
			transacation.setMoney_symbol("R$");
			transacation.setDescription_transaction("Deposit in account " + transacation.getAccountId());
			if (transacation.getGuidId() == null) {
				UUID uuidTo = UUID.randomUUID();
				transacation.setGuidId(uuidTo.toString());
				System.out.println("UUID=" + uuidTo.toString());
			}
			movimentacaoDetailsService.addTransaction(transacation);
			LOGGER.info("Deposit was created");
			return new ResponseEntity<>(addFeedbackMessage(FEEDBACK_MESSAGE_DEPOSIT_CREATED), HttpStatus.CREATED);

		} catch (Exception e) {
			LOGGER.error("Error information " + e);
			System.out.println(e);
			e.printStackTrace();
			return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_DEPOSIT_NOT_AVAILABLE),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping(value = "/transferByCPF", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createdInternTransfer(@RequestBody TransferDTO transfer,
			@RequestHeader("Authorization") String auth) throws Exception {
		try {

			LOGGER.info("Starting Transfer api");
			LOGGER.info("Transfer By Cpf has started");
			System.out.println("teste auth  " + auth);
			String token = auth.replaceAll("Bearer ", "");

			String cpf = getCpfFromToken(token);

			double value = transfer.getValue();
			if (transfer.getValue() <= 0) {
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_TRANSFER_VALUE_NOT_VALID),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			int accountFrom = returnAccount(cpf, transfer.getType_account_from());
			int accountTo = returnAccount(transfer.getDocument_to(), transfer.getType_account_to());

			if (accountTo == 0) {
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_ACCOUNT_NUMBER_NOT_FOUND),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			System.out.println("Account from  =" + accountFrom);
			System.out.println("Account to  =" + accountTo);
			LOGGER.info("Account from is " + accountFrom);
			LOGGER.info("Account to is " + accountTo);
			LOGGER.info("Value of transaction is " + transfer.getValue());

			if (accountFrom == accountTo) {
				LOGGER.error("Same account number AccountFrom and AccountTo");
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_ACCOUNT_NUMBER_NOT_FOUND),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			Double balanceOldFrom = movimentacaoDetailsService.getBalance(accountFrom, 1);
			Double balanceOldTo = movimentacaoDetailsService.getBalance(accountTo, 1);

			if ((balanceOldFrom - value) < 0)
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_NO_MONEY_TO_THIS_TRANSACTION),
						HttpStatus.INTERNAL_SERVER_ERROR);
			System.out.print("Log antes de fazer");
			System.out.print("Log antes de fazer retirada FROM " + balanceOldFrom);
			TransactionDTO withDrawal = new TransactionDTO();
			TransactionDTO deposit = new TransactionDTO();

			if ((balanceOldFrom - value) >= 0) {
				try {
					withDrawal.setAccountId(accountFrom);
					withDrawal.setValue(transfer.getValue());
					withDrawal.setDescription_extract(transfer.getDescription_extract());
					withDrawal.setDescription_transaction("Trasfer from 1 to 2");
					UUID uuidFrom = UUID.randomUUID();
					System.out.println("UUID =" + uuidFrom.toString());
					withDrawal.setGuidId(uuidFrom.toString());
					createWithDrawal(withDrawal);
					LOGGER.info("WithDrawal worked");
				} catch (Exception e) {
					LOGGER.error("WithDrawal did not work");
					throw new Exception("WithDrawal did not work");
				}
				try {
					deposit.setAccountId(accountTo);
					deposit.setValue(value);
					deposit.setDescription_transaction("Trasfer came from 2");
					deposit.setDescription_extract("Trasfer from 1 to 2");
					UUID uuidTo = UUID.randomUUID();
					deposit.setGuidId(uuidTo.toString());
					System.out.println("UUID=" + uuidTo.toString());
					LOGGER.info("Deposit worked");
					createDeposit(deposit);
				} catch (Exception e) {
					movimentacaoDetailsService.deleteTransacationByGuid(withDrawal);
					movimentacaoDetailsService.deleteTransacationByGuid(deposit);
					LOGGER.error("Deposit did not work");
					throw new Exception("Deposit did not work");
				}
			}

			Double balanceNewFrom = movimentacaoDetailsService.getBalance(accountFrom, 1);
			Double balanceNewTo = movimentacaoDetailsService.getBalance(accountTo, 1);
			System.out.println("Balanco oldTO " + balanceOldTo);
			System.out.println("Balanco newTO " + balanceNewTo);
			System.out.println("Balanco oldFrom " + balanceOldFrom);
			System.out.println("Balanco newFrom " + balanceNewFrom);
			
			if ((balanceOldFrom - value != balanceNewFrom)) {
				LOGGER.error("Balance of account " + accountFrom + " is not right after transacation");
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_WRONG_BALANCE_AFTER_TRANSACTION),
						HttpStatus.INTERNAL_SERVER_ERROR);

			}

			
			if ((balanceOldTo + value != balanceNewTo)) {
				LOGGER.error("Balance of account " + accountTo + " is not right after transacation");
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_DEPOSIT_VALUE_NOT_VALID),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			LOGGER.info("Balance Account of account "+accountFrom+" before this transaction = "+ balanceOldFrom); 
			LOGGER.info("Balance Account of account "+accountFrom+" after this transaction = "+ balanceNewFrom); 
			LOGGER.info("Balance Account of account "+accountTo+" before this transaction = "+ balanceOldTo); 
			LOGGER.info("Balance Account of account "+accountTo+" after this transaction = "+ balanceNewTo); 
			  
			LOGGER.info("Transfer was created"); 
			return new ResponseEntity<>(addFeedbackMessage(FEEDBACK_MESSAGE_TRANSFER_CREATED_SUCCESS),
					HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e);
			LOGGER.error("Transfer went wrong");
			LOGGER.error(e);
			LOGGER.error(e.getStackTrace());
			return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_TRANSFER_UNAVAILABLE),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/withdrawal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createWithDrawal(@RequestBody TransactionDTO transacation) throws Exception {
		try {
			LOGGER.info("Withdrawal has started");
			if ((transacation.getValue() <= 0)) {
				LOGGER.error("Value of transfer has to be bigger than zero");
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_WITHDRAWAL_VALUE_NOT_VALID),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			Double balanceOld = movimentacaoDetailsService.getBalance(transacation.getAccountId(), 1);
			if (balanceOld - transacation.getValue() < 0) {
				LOGGER.error("User does not have enough money to this transaction");
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_NO_MONEY_TO_THIS_TRANSACTION),
						HttpStatus.INTERNAL_SERVER_ERROR);

			}
			LOGGER.info("Value of transaction is " + transacation.getValue());
			LOGGER.info("Account of transaction is " + transacation.getAccountId());

			double value = (transacation.getValue());
			transacation.setValue(value * -1);
			transacation.setDescriptionStatus("Approved");
			transacation.setStatusTransaction(1);
			transacation.setType_operation("Debit");
			transacation.setMoney_name("BRL");
			transacation.setTypeTransactionId(1);
			transacation.setDescription_type_transaction("Out - Withdrawal");
			transacation.setDescription_transaction("Withdrawal in account " + transacation.getAccountId());
			transacation.setMoney_symbol("R$");
			if (transacation.getGuidId() == null) {
				UUID uuidTo = UUID.randomUUID();
				transacation.setGuidId(uuidTo.toString());
				System.out.println("UUID=" + uuidTo.toString());
			}
			movimentacaoDetailsService.addTransaction(transacation);
			Double balanceNew = movimentacaoDetailsService.getBalance(transacation.getAccountId(), 1);
			System.out.print("old" + balanceOld);
			System.out.print("new" + balanceNew);

			System.out.print("t " + transacation.getValue());
			
			if ((balanceOld - value != balanceNew)) {
				LOGGER.error("Wrong balance after an withdrawal");
				return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_WRONG_BALANCE_AFTER_TRANSACTION),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

			LOGGER.info("Withdrawal has finished");
			return new ResponseEntity<>(addFeedbackMessage(FEEDBACK_MESSAGE_WITHDRAWAL_CREATED), HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_WITHDRAWAL_UNAVAILABLE),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/extract", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getExtract(@RequestBody TransactionDTO transacation,
			@RequestHeader("Authorization") String auth) throws Exception {
		try {

			LOGGER.info("Extract has started");
			String token = auth.replaceAll("Bearer ", "");

			String cpf = getCpfFromToken(token);

			int contaCorrenteId = returnAccount(cpf, 1);
			;
			int contaPoupancaId = returnAccount(cpf, 2);

			List<TransactionDao> transactionsContaCorrente = movimentacaoDetailsService
					.searchByAccountId(contaCorrenteId, 1);

			Extrato extratoContaCorrente = new Extrato("Conta Corrente", "BRL", "R$", transactionsContaCorrente);

			List<TransactionDao> transactionsContaPoupanca = movimentacaoDetailsService
					.searchByAccountId(contaPoupancaId, 1);
			Extrato extratoContaPoupanca = new Extrato("Conta Poupança", "BRL", "R$", transactionsContaPoupanca);

			LOGGER.info("Extract was gotten");
			List<Extrato> listaExtrato = new ArrayList<Extrato>();
			listaExtrato.add(extratoContaPoupanca);
			listaExtrato.add(extratoContaCorrente);
			return new ResponseEntity<>(addFeedbackMessage(FEEDBACK_MESSAGE_ALL_TRANSACTIONS_AVAILABLE, listaExtrato),
					HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(ERROR_MESSAGE_ALL_TRANSACTIONS_NOT_AVAILABLE);
			LOGGER.error(e);
			LOGGER.error(e.getStackTrace());
			return new ResponseEntity<>(addErrorMessage(ERROR_MESSAGE_ALL_TRANSACTIONS_NOT_AVAILABLE),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
