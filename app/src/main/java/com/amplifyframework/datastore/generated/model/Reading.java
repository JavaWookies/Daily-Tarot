package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Reading type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Readings", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Reading implements Model {
  public static final QueryField ID = field("Reading", "id");
  public static final QueryField USER_ID = field("Reading", "userId");
  public static final QueryField DATE_CREATED = field("Reading", "dateCreated");
  public static final QueryField CARD_ONE_ID = field("Reading", "cardOneId");
  public static final QueryField CARD_ONE_ORIENTATION = field("Reading", "cardOneOrientation");
  public static final QueryField CARD_TWO_ID = field("Reading", "cardTwoId");
  public static final QueryField CARD_TWO_ORIENTATION = field("Reading", "cardTwoOrientation");
  public static final QueryField CARD_THREE_ID = field("Reading", "cardThreeId");
  public static final QueryField CARD_THREE_ORIENTATION = field("Reading", "cardThreeOrientation");
  public static final QueryField INTERPRETATION = field("Reading", "interpretation");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String userId;
  private final @ModelField(targetType="String", isRequired = true) String dateCreated;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardOneId;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardOneOrientation;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardTwoId;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardTwoOrientation;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardThreeId;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardThreeOrientation;
  private final @ModelField(targetType="String", isRequired = true) String interpretation;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return userId;
  }
  
  public String getDateCreated() {
      return dateCreated;
  }
  
  public Integer getCardOneId() {
      return cardOneId;
  }
  
  public Integer getCardOneOrientation() {
      return cardOneOrientation;
  }
  
  public Integer getCardTwoId() {
      return cardTwoId;
  }
  
  public Integer getCardTwoOrientation() {
      return cardTwoOrientation;
  }
  
  public Integer getCardThreeId() {
      return cardThreeId;
  }
  
  public Integer getCardThreeOrientation() {
      return cardThreeOrientation;
  }
  
  public String getInterpretation() {
      return interpretation;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Reading(String id, String userId, String dateCreated, Integer cardOneId, Integer cardOneOrientation, Integer cardTwoId, Integer cardTwoOrientation, Integer cardThreeId, Integer cardThreeOrientation, String interpretation) {
    this.id = id;
    this.userId = userId;
    this.dateCreated = dateCreated;
    this.cardOneId = cardOneId;
    this.cardOneOrientation = cardOneOrientation;
    this.cardTwoId = cardTwoId;
    this.cardTwoOrientation = cardTwoOrientation;
    this.cardThreeId = cardThreeId;
    this.cardThreeOrientation = cardThreeOrientation;
    this.interpretation = interpretation;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Reading reading = (Reading) obj;
      return ObjectsCompat.equals(getId(), reading.getId()) &&
              ObjectsCompat.equals(getUserId(), reading.getUserId()) &&
              ObjectsCompat.equals(getDateCreated(), reading.getDateCreated()) &&
              ObjectsCompat.equals(getCardOneId(), reading.getCardOneId()) &&
              ObjectsCompat.equals(getCardOneOrientation(), reading.getCardOneOrientation()) &&
              ObjectsCompat.equals(getCardTwoId(), reading.getCardTwoId()) &&
              ObjectsCompat.equals(getCardTwoOrientation(), reading.getCardTwoOrientation()) &&
              ObjectsCompat.equals(getCardThreeId(), reading.getCardThreeId()) &&
              ObjectsCompat.equals(getCardThreeOrientation(), reading.getCardThreeOrientation()) &&
              ObjectsCompat.equals(getInterpretation(), reading.getInterpretation()) &&
              ObjectsCompat.equals(getCreatedAt(), reading.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), reading.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getDateCreated())
      .append(getCardOneId())
      .append(getCardOneOrientation())
      .append(getCardTwoId())
      .append(getCardTwoOrientation())
      .append(getCardThreeId())
      .append(getCardThreeOrientation())
      .append(getInterpretation())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Reading {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userId=" + String.valueOf(getUserId()) + ", ")
      .append("dateCreated=" + String.valueOf(getDateCreated()) + ", ")
      .append("cardOneId=" + String.valueOf(getCardOneId()) + ", ")
      .append("cardOneOrientation=" + String.valueOf(getCardOneOrientation()) + ", ")
      .append("cardTwoId=" + String.valueOf(getCardTwoId()) + ", ")
      .append("cardTwoOrientation=" + String.valueOf(getCardTwoOrientation()) + ", ")
      .append("cardThreeId=" + String.valueOf(getCardThreeId()) + ", ")
      .append("cardThreeOrientation=" + String.valueOf(getCardThreeOrientation()) + ", ")
      .append("interpretation=" + String.valueOf(getInterpretation()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserIdStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Reading justId(String id) {
    return new Reading(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userId,
      dateCreated,
      cardOneId,
      cardOneOrientation,
      cardTwoId,
      cardTwoOrientation,
      cardThreeId,
      cardThreeOrientation,
      interpretation);
  }
  public interface UserIdStep {
    DateCreatedStep userId(String userId);
  }
  

  public interface DateCreatedStep {
    CardOneIdStep dateCreated(String dateCreated);
  }
  

  public interface CardOneIdStep {
    CardOneOrientationStep cardOneId(Integer cardOneId);
  }
  

  public interface CardOneOrientationStep {
    CardTwoIdStep cardOneOrientation(Integer cardOneOrientation);
  }
  

  public interface CardTwoIdStep {
    CardTwoOrientationStep cardTwoId(Integer cardTwoId);
  }
  

  public interface CardTwoOrientationStep {
    CardThreeIdStep cardTwoOrientation(Integer cardTwoOrientation);
  }
  

  public interface CardThreeIdStep {
    CardThreeOrientationStep cardThreeId(Integer cardThreeId);
  }
  

  public interface CardThreeOrientationStep {
    InterpretationStep cardThreeOrientation(Integer cardThreeOrientation);
  }
  

  public interface InterpretationStep {
    BuildStep interpretation(String interpretation);
  }
  

  public interface BuildStep {
    Reading build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserIdStep, DateCreatedStep, CardOneIdStep, CardOneOrientationStep, CardTwoIdStep, CardTwoOrientationStep, CardThreeIdStep, CardThreeOrientationStep, InterpretationStep, BuildStep {
    private String id;
    private String userId;
    private String dateCreated;
    private Integer cardOneId;
    private Integer cardOneOrientation;
    private Integer cardTwoId;
    private Integer cardTwoOrientation;
    private Integer cardThreeId;
    private Integer cardThreeOrientation;
    private String interpretation;
    @Override
     public Reading build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Reading(
          id,
          userId,
          dateCreated,
          cardOneId,
          cardOneOrientation,
          cardTwoId,
          cardTwoOrientation,
          cardThreeId,
          cardThreeOrientation,
          interpretation);
    }
    
    @Override
     public DateCreatedStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userId = userId;
        return this;
    }
    
    @Override
     public CardOneIdStep dateCreated(String dateCreated) {
        Objects.requireNonNull(dateCreated);
        this.dateCreated = dateCreated;
        return this;
    }
    
    @Override
     public CardOneOrientationStep cardOneId(Integer cardOneId) {
        Objects.requireNonNull(cardOneId);
        this.cardOneId = cardOneId;
        return this;
    }
    
    @Override
     public CardTwoIdStep cardOneOrientation(Integer cardOneOrientation) {
        Objects.requireNonNull(cardOneOrientation);
        this.cardOneOrientation = cardOneOrientation;
        return this;
    }
    
    @Override
     public CardTwoOrientationStep cardTwoId(Integer cardTwoId) {
        Objects.requireNonNull(cardTwoId);
        this.cardTwoId = cardTwoId;
        return this;
    }
    
    @Override
     public CardThreeIdStep cardTwoOrientation(Integer cardTwoOrientation) {
        Objects.requireNonNull(cardTwoOrientation);
        this.cardTwoOrientation = cardTwoOrientation;
        return this;
    }
    
    @Override
     public CardThreeOrientationStep cardThreeId(Integer cardThreeId) {
        Objects.requireNonNull(cardThreeId);
        this.cardThreeId = cardThreeId;
        return this;
    }
    
    @Override
     public InterpretationStep cardThreeOrientation(Integer cardThreeOrientation) {
        Objects.requireNonNull(cardThreeOrientation);
        this.cardThreeOrientation = cardThreeOrientation;
        return this;
    }
    
    @Override
     public BuildStep interpretation(String interpretation) {
        Objects.requireNonNull(interpretation);
        this.interpretation = interpretation;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String userId, String dateCreated, Integer cardOneId, Integer cardOneOrientation, Integer cardTwoId, Integer cardTwoOrientation, Integer cardThreeId, Integer cardThreeOrientation, String interpretation) {
      super.id(id);
      super.userId(userId)
        .dateCreated(dateCreated)
        .cardOneId(cardOneId)
        .cardOneOrientation(cardOneOrientation)
        .cardTwoId(cardTwoId)
        .cardTwoOrientation(cardTwoOrientation)
        .cardThreeId(cardThreeId)
        .cardThreeOrientation(cardThreeOrientation)
        .interpretation(interpretation);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder dateCreated(String dateCreated) {
      return (CopyOfBuilder) super.dateCreated(dateCreated);
    }
    
    @Override
     public CopyOfBuilder cardOneId(Integer cardOneId) {
      return (CopyOfBuilder) super.cardOneId(cardOneId);
    }
    
    @Override
     public CopyOfBuilder cardOneOrientation(Integer cardOneOrientation) {
      return (CopyOfBuilder) super.cardOneOrientation(cardOneOrientation);
    }
    
    @Override
     public CopyOfBuilder cardTwoId(Integer cardTwoId) {
      return (CopyOfBuilder) super.cardTwoId(cardTwoId);
    }
    
    @Override
     public CopyOfBuilder cardTwoOrientation(Integer cardTwoOrientation) {
      return (CopyOfBuilder) super.cardTwoOrientation(cardTwoOrientation);
    }
    
    @Override
     public CopyOfBuilder cardThreeId(Integer cardThreeId) {
      return (CopyOfBuilder) super.cardThreeId(cardThreeId);
    }
    
    @Override
     public CopyOfBuilder cardThreeOrientation(Integer cardThreeOrientation) {
      return (CopyOfBuilder) super.cardThreeOrientation(cardThreeOrientation);
    }
    
    @Override
     public CopyOfBuilder interpretation(String interpretation) {
      return (CopyOfBuilder) super.interpretation(interpretation);
    }
  }
  

  public static class ReadingIdentifier extends ModelIdentifier<Reading> {
    private static final long serialVersionUID = 1L;
    public ReadingIdentifier(String id) {
      super(id);
    }
  }
  
}
