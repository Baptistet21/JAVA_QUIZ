//package fr.insa.quiz_insa.repository;
//
//import fr.insa.quiz_insa.model.Questionnaire;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//
//public interface QuestionnaireRepository extends CrudRepository<Questionnaire,Long> {
//    @Query("SELECT q FROM Questionnaire q " +
//            "WHERE " +
//            "q.theme LIKE %:filtre% ")
//    Iterable<Questionnaire> findByFiltre(String filtre);
//
//}