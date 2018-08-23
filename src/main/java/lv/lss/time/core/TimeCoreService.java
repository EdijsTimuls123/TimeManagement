package lv.lss.time.core;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.lss.time.dto.EventDTO;
import lv.lss.time.jpa.Event;
import lv.lss.time.jpa.User;

@Stateless
public class TimeCoreService implements TimeCoreServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(TimeCoreService.class);
	
	@PersistenceContext(name="timePU")
	private EntityManager entityManager;

	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUser(User u) {
		entityManager.persist(u);
		entityManager.flush();
	    entityManager.clear();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Transactional
	public User getUserById(Integer id) {
		
		String selectQuery = "SELECT u FROM User u WHERE u.id= :id";
		
		Query userQuery = entityManager.createQuery(selectQuery);
		
		userQuery.setParameter("id", id);
		
		return (User) userQuery.getResultList()
			.stream()
			.findFirst()
			.orElse(null);
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
		                     " LEFT JOIN u.events" +
		                     " WHERE u.name= :name AND u.password= :password";
		
		Query loginQuery = entityManager.createQuery(selectQuery);
		
		loginQuery.setParameter("name", name);
		loginQuery.setParameter("password", password);
				
		return (User) loginQuery.getResultList()
			.stream()
			.findFirst()
			.orElse(null);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Transactional
	public void deleteEventById(Integer id){
		String deleteQuery = "DELETE FROM Event e WHERE e.id=:id";
		Query eventQuery = entityManager.createQuery(deleteQuery);
		eventQuery.setParameter("id", id);
		eventQuery.executeUpdate();
	}

	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveEvent(Event e) {
		String updateQuery = "UPDATE Event SET info=:info, startDate=:startDate, endDate=:endDate WHERE id=:id";
		Query eventQuery = entityManager.createQuery(updateQuery);
		eventQuery.setParameter("id", e.getId());
		eventQuery.setParameter("info", e.getInfo());
		eventQuery.setParameter("startDate", e.getStartDate());
		eventQuery.setParameter("endDate", e.getEndDate());
		eventQuery.executeUpdate();
	}

	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer createEvent(Event e) {
		Event newEvent = entityManager.merge(e);
		entityManager.flush();
	    entityManager.clear();
		return newEvent.getId();
	}

	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean validateEvent(Event e) {
		String selectQuery = "SELECT u" +
                " FROM User u" +
                " INNER JOIN u.events e" +
                " WHERE (   (:startDate BETWEEN e.startDate AND e.endDate)" +
                "        OR (:endDate BETWEEN e.startDate AND e.endDate)" +
                "        OR (e.startDate BETWEEN :startDate AND :endDate AND e.endDate BETWEEN :startDate AND :endDate ) )" +
                "   AND u.id = :userId";
		if (e.getId() > 0) {
			selectQuery += " AND e.id <> :id";
		}

		Query eventQuery = entityManager.createQuery(selectQuery);
		
		eventQuery.setParameter("userId", e.getUser().getId());
		eventQuery.setParameter("startDate", e.getStartDate());
		eventQuery.setParameter("endDate", e.getEndDate());
		if (e.getId() > 0) {
			eventQuery.setParameter("id", e.getId());
		}
			
		return eventQuery.getResultList().size() == 0;
	}

}
