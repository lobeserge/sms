package ub.fet.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ub.fet.sms.model.Student;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByName(String username);
	Boolean existsByName(String username);

}
