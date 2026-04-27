package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepoImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSentimentAnalysis(){
        Query query = new Query();
//        query.addCriteria(Criteria.where("userName").is("vipul"));

        // can be chained too
        query.addCriteria(Criteria.where("email").exists(true).ne(null).ne(""));
//        query.addCriteria(Criteria.where("email").ne(null).ne(""));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        // can also add regex to do email check
//        query.addCriteria(Criteria.where("email").regex(""))

        // can do range queries using lt, lte, gt, gte
//        query.addCriteria(Criteria.where("email").lte());


        // can also be written like this
//        Criteria criteria = new Criteria();
//        query.addCriteria(criteria.andOperator(
//                Criteria.where("email").exists(true),
//                Criteria.where("sentimentAnalysis").is(true)
//        ));

        // can similarly have orOperator

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
