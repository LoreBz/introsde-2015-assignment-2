package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.LifeCoachDao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity
// indicates that this class is an entity to persist in DB
@Cacheable(false)
@Table(name = "Person")
// to whole table must be persisted
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@XmlRootElement
@XmlType(propOrder = { "name", "lastname", "birthdate", "lifeStatus" })
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	// defines this attributed as the one that identifies the entity
	@GeneratedValue(generator = "sqlite_person")
	@TableGenerator(name = "sqlite_person", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Person")
	@Column(name = "idPerson")
	private int idPerson;
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "name")
	private String name;
	@Column(name = "username")
	private String username;
	@Temporal(TemporalType.DATE)
	// defines the precision of the date attribute
	@Column(name = "birthdate")
	private Date birthdate;
	@Column(name = "email")
	private String email;

	// mappedBy must be equal to the name of the attribute in LifeStatus that
	// maps this relation
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<LifeStatus> lifeStatus;

	@XmlElementWrapper(name = "healthProfile")
	public List<LifeStatus> getLifeStatus() {
		return lifeStatus;
	}

	// add below all the getters and setters of all the private attributes

	// getters
	@XmlElement(name="personID")
	public int getIdPerson() {
		return idPerson;
	}

	public String getLastname() {
		return lastname;
	}

	@XmlElement(name = "firstname")
	public String getName() {
		return name;
	}

	@XmlTransient
	public String getUsername() {
		return username;
	}

	public String getBirthdate() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		// Get the date today using Calendar object.
		return df.format(birthdate);
	}

	@XmlTransient
	public String getEmail() {
		return email;
	}

	// setters
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setLifeStatus(List<LifeStatus> lifeStatus) {
		this.lifeStatus = lifeStatus;
	}

	public void setBirthdate(String bd) throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date date = format.parse(bd);
		this.birthdate = date;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static Person getPersonById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static List<Person> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
				.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	public static Person savePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static Person updatePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static void removePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}

}