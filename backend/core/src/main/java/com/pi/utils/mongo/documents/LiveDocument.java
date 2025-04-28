package com.pi.utils.mongo.documents;

import com.pi.core_live.core.domain.Live;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.ObjectUtils;

import static com.pi.utils.mongo.constants.Collections.COLLECTION_LIVES;

@Document(collection = COLLECTION_LIVES)
public class LiveDocument extends Live {
    @Id String id;

    public LiveDocument() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public static LiveDocument mapperDocument(Live live) {
        if (ObjectUtils.isEmpty(live)) return null;
        var doc = new LiveDocument();
        doc.key(live.getKey());
        doc.startedOn(live.getStartedOn());
        doc.updateOn(live.getUpdateOn());
        doc.completedOn(live.getCompletedOn());
        doc.status(live.getStatus());
        doc.engagement(live.getEngagement());
        doc.evaluation(live.getEvaluation());
        doc.quiz(live.getQuiz());
        doc.teacher(live.getTeacher());
        doc.lobby(live.getLobby());
        return doc;
    }

    public static Live mapperLive(LiveDocument liveDocument) {
        if (ObjectUtils.isEmpty(liveDocument)) return null;
        return Live.builder()
                .key(liveDocument.getKey())
                .startedOn(liveDocument.getStartedOn())
                .updateOn(liveDocument.getUpdateOn())
                .completedOn(liveDocument.getCompletedOn())
                .status(liveDocument.getStatus())
                .engagement(liveDocument.getEngagement())
                .evaluation(liveDocument.getEvaluation())
                .quiz(liveDocument.getQuiz())
                .teacher(liveDocument.getTeacher())
                .lobby(liveDocument.getLobby())
                .build();
    }
}
