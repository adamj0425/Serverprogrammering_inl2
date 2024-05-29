package se.yrgo.test;

import jakarta.persistence.*;

import se.yrgo.domain.Student;
import se.yrgo.domain.Subject;
import se.yrgo.domain.Tutor;

import java.util.List;

public class HibernateTest
{
	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("databaseConfig");

	public static void main(String[] args){
		setUpData();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();


		//Uppgift 1
		Subject science = em.find(Subject.class, 2);
		TypedQuery<String> query= em.createQuery("select student.name from Tutor tutor join tutor.teachingGroup student where :subject member of tutor.subjectsToTeach", String.class);
		query.setParameter("subject", science);
		List<String> studentsWithScienceTeacher = query.getResultList();
		for(String student : studentsWithScienceTeacher) {
			System.out.println(student);
		}

		//Uppgift 2
		List<Object[]>results = em.createQuery("select student.name, tutor.name from Tutor tutor join tutor.teachingGroup student").getResultList();
		for(Object[] obj:results) {
			System.out.println("Name: " + obj[0]);
			System.out.println("ID: " + obj[1]);
		}

		//Uppgift 3
		double averageNumberOfSemesters = (Double)em.createQuery("select avg(numberOfSemesters)from Subject subject").getSingleResult();
		System.out.println("The average number of semesters is " + averageNumberOfSemesters);

		//Uppgift 4
		int maxSalary = (int)em.createQuery("select max(salary)from Tutor tutor").getSingleResult();
		System.out.println("The max salary is " + maxSalary);

		//Uppgift 5
		List<Tutor> tutorResults = em.createNamedQuery("searchByMinimumSalary", Tutor.class).setParameter("salary", 10000).getResultList();
		for(Tutor tutor: tutorResults) {
			System.out.println(tutor);
		}

		tx.commit();
		em.close();
	}

	public static void setUpData(){
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();


		Subject mathematics = new Subject("Mathematics", 2);
		Subject science = new Subject("Science", 2);
		Subject programming = new Subject("Programming", 3);
		em.persist(mathematics);
		em.persist(science);
		em.persist(programming);

		Tutor t1 = new Tutor("ABC123", "Johan Smith", 40000);
		t1.addSubjectsToTeach(mathematics);
		t1.addSubjectsToTeach(science);


		Tutor t2 = new Tutor("DEF456", "Sara Svensson", 20000);
		t2.addSubjectsToTeach(mathematics);
		t2.addSubjectsToTeach(science);

		// This tutor is the only tutor who can teach History
		Tutor t3 = new Tutor("GHI678", "Karin Lindberg", 0);
		t3.addSubjectsToTeach(programming);

		em.persist(t1);
		em.persist(t2);
		em.persist(t3);


		t1.createStudentAndAddtoTeachingGroup("Jimi Hendriks", "1-HEN-2019", "Street 1", "city 2", "1212");
		t1.createStudentAndAddtoTeachingGroup("Bruce Lee", "2-LEE-2019", "Street 2", "city 2", "2323");
		t3.createStudentAndAddtoTeachingGroup("Roger Waters", "3-WAT-2018", "Street 3", "city 3", "34343");

		tx.commit();
		em.close();
	}


}
