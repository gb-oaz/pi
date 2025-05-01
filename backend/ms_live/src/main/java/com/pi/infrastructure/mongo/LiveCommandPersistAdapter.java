package com.pi.infrastructure.mongo;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.ports.out.ILiveCommandPersistOut;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.mongo.documents.LiveDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

import java.util.Objects;

@Repository
public class LiveCommandPersistAdapter implements ILiveCommandPersistOut {
    @Value("${spring.data.mongodb.collections.lives}") String COLLECTION_NAME;

    private final MongoTemplate template;

    @Autowired
    public LiveCommandPersistAdapter(MongoTemplate template) { this.template = template; }

    @Override
    public Mono<Live> persistLive(Live live) throws GlobalException {
        var document = template.insert(Objects.requireNonNull(LiveDocument.mapperDocument(live)), COLLECTION_NAME);
        return Mono.just(Objects.requireNonNull(LiveDocument.mapperLive(document)));
    }
}
