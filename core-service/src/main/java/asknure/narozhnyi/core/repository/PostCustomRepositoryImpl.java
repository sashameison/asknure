package asknure.narozhnyi.core.repository;

import static asknure.narozhnyi.core.dto.PostSearchParam.Fields.categories;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import asknure.narozhnyi.core.config.SearchProperties;
import asknure.narozhnyi.core.dto.PostSearchParam;
import asknure.narozhnyi.core.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

  private final MongoTemplate mongoTemplate;
  private final SearchProperties searchProperties;

  @Override
  public Page<Post> findPostBy(Pageable pageable, PostSearchParam postSearchParam) {
    var query = new Query();
    var criteria = buildCriteria(postSearchParam);
    if (isNotEmpty(criteria)) {
      query.addCriteria(new Criteria().andOperator(criteria.toArray(Criteria[]::new)));
    }
    query.with(pageable);
    var count = mongoTemplate.count(query, Post.class);
    var posts = mongoTemplate.find(query, Post.class);
    return new PageImpl<>(posts, pageable, count);
  }

  public List<Criteria> buildCriteria(PostSearchParam postSearchParam) {
    var criteria = new ArrayList<Criteria>();
    addRegexCriteria(postSearchParam.getSearchParam(), searchProperties.getFields())
        .ifPresent(criteria::add);
    if (isNotEmpty(postSearchParam.getCategories())) {
      criteria.add(Criteria.where(categories).in(postSearchParam.getCategories()));
    }
    return criteria;
  }

  private Optional<Criteria> addRegexCriteria(String searchParam, List<String> searchFields) {
    var regex =
        MongoRegexCreator.INSTANCE.toRegularExpression(searchParam, MongoRegexCreator.MatchMode.CONTAINING);

    return Optional.ofNullable(regex)
        .map(searchKey -> searchFields.stream()
            .map(searchField -> Criteria.where(searchField).regex(regex, "i"))
            .toArray(Criteria[]::new))
        .map(searchCriteria -> new Criteria().orOperator(searchCriteria));
  }
}
