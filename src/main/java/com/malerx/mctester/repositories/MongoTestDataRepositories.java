package com.malerx.mctester.repositories;

import com.malerx.mctester.model.Chain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoTestDataRepositories extends MongoRepository<Chain, String> {

}
