package org.example.spring.service;

import org.example.spring.model.Greeting;
import org.example.spring.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GreetingServiceBeanJpa implements GreetingService {
    @Autowired
    private GreetingRepository greetingRepository;

    @Override
    public Collection<Greeting> findAll() {

        Collection<Greeting> greetings = greetingRepository.findAll();

        return greetings;
    }

    @Override
    public Greeting findOne(Long id) {

        Greeting greeting = greetingRepository.findOne(id);

        return greeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Greeting create(Greeting greeting) {

        // Ensure the entity object to be created does NOT exist in the
        // repository. Prevent the default behavior of save() which will update
        // an existing entity if the entity matching the supplied id exists.
        if (greeting.getId() != null) {
            // Cannot create Greeting with specified ID value
            return null;
        }

        Greeting savedGreeting = greetingRepository.save(greeting);
        if (savedGreeting.getId() == 7){
            throw new RuntimeException();
        }

        return savedGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Greeting update(Greeting greeting) {

        // Ensure the entity object to be updated exists in the repository to
        // prevent the default behavior of save() which will persist a new
        // entity if the entity matching the id does not exist
        Greeting greetingToUpdate = findOne(greeting.getId());
        if (greetingToUpdate == null) {
            // Cannot update Greeting that hasn't been persisted
            return null;
        }

        greetingToUpdate.setText(greeting.getText());
        Greeting updatedGreeting = greetingRepository.save(greetingToUpdate);

        return updatedGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) {

        greetingRepository.delete(id);

    }

}