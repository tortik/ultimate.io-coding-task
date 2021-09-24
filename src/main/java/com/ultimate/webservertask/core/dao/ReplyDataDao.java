package com.ultimate.webservertask.core.dao;

import com.ultimate.webservertask.core.model.db.ReplyData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyDataDao extends MongoRepository<ReplyData, String> {

  Optional<ReplyData> findByIntentAndThresholdLessThanEqual(String intent, double precision);

}
