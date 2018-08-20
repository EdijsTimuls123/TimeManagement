package lv.lss.time.core;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import lv.lss.time.jpa.User;

@Stateless
public class TimeCoreService implements TimeCoreServiceInterface {

	@PersistenceContext(name="timePU")
	private EntityManager entityManager;

	
	
	
	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUser(User u) {
		entityManager.persist(u);
		entityManager.flush();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Transactional
	public User getUserByName(String name) {
		
		String selectQuery = "SELECT u FROM User u WHERE u.name= :name";
		
		Query userQuery = entityManager.createQuery(selectQuery);
		
		userQuery.setParameter("name", name);
		
		return (User) userQuery.getResultList()
			.stream()
			.findFirst()
			.orElse(null);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Transactional
	public User loginUser(String name, String password) {
		
		String selectQuery = "SELECT u" +
		                     " FROM User u" +
		                     " INNER JOIN u.events" +
		                     " WHERE u.name= :name AND u.password= :password";
		
		Query loginQuery = entityManager.createQuery(selectQuery);
		
		loginQuery.setParameter("name", name);
		loginQuery.setParameter("password", password);
		
		return (User) loginQuery.getResultList()
			.stream()
			.findFirst()
			.orElse(null);
	}

	
}
