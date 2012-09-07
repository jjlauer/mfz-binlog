package com.mfizz.binlog.protobuf;

/*
 * #%L
 * mfz-binlog
 * %%
 * Copyright (C) 2012 mfizz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public final class BinlogProtos {
  private BinlogProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface KeyValueOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional string key = 1;
    boolean hasKey();
    String getKey();
    
    // optional string value = 2;
    boolean hasValue();
    String getValue();
  }
  public static final class KeyValue extends
      com.google.protobuf.GeneratedMessage
      implements KeyValueOrBuilder {
    // Use KeyValue.newBuilder() to construct.
    private KeyValue(Builder builder) {
      super(builder);
    }
    private KeyValue(boolean noInit) {}
    
    private static final KeyValue defaultInstance;
    public static KeyValue getDefaultInstance() {
      return defaultInstance;
    }
    
    public KeyValue getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_KeyValue_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_KeyValue_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional string key = 1;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          key_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string value = 2;
    public static final int VALUE_FIELD_NUMBER = 2;
    private java.lang.Object value_;
    public boolean hasValue() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getValue() {
      java.lang.Object ref = value_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          value_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getValueBytes() {
      java.lang.Object ref = value_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        value_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      key_ = "";
      value_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getValueBytes());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getValueBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.KeyValue parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.mfizz.binlog.protobuf.BinlogProtos.KeyValue prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_KeyValue_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_KeyValue_fieldAccessorTable;
      }
      
      // Construct using com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.getDescriptor();
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue getDefaultInstanceForType() {
        return com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.getDefaultInstance();
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue build() {
        com.mfizz.binlog.protobuf.BinlogProtos.KeyValue result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.mfizz.binlog.protobuf.BinlogProtos.KeyValue buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.mfizz.binlog.protobuf.BinlogProtos.KeyValue result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue buildPartial() {
        com.mfizz.binlog.protobuf.BinlogProtos.KeyValue result = new com.mfizz.binlog.protobuf.BinlogProtos.KeyValue(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.mfizz.binlog.protobuf.BinlogProtos.KeyValue) {
          return mergeFrom((com.mfizz.binlog.protobuf.BinlogProtos.KeyValue)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.mfizz.binlog.protobuf.BinlogProtos.KeyValue other) {
        if (other == com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.getDefaultInstance()) return this;
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              key_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              value_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional string key = 1;
      private java.lang.Object key_ = "";
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          key_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setKey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      void setKey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
      }
      
      // optional string value = 2;
      private java.lang.Object value_ = "";
      public boolean hasValue() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getValue() {
        java.lang.Object ref = value_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          value_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setValue(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
        return this;
      }
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000002);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }
      void setValue(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:binlog.KeyValue)
    }
    
    static {
      defaultInstance = new KeyValue(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:binlog.KeyValue)
  }
  
  public interface BinlogFileHeaderOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional fixed64 createDateTime = 1;
    boolean hasCreateDateTime();
    long getCreateDateTime();
    
    // optional string name = 2;
    boolean hasName();
    String getName();
    
    // optional string contentType = 3;
    boolean hasContentType();
    String getContentType();
    
    // repeated .binlog.KeyValue optionalParameter = 4;
    java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> 
        getOptionalParameterList();
    com.mfizz.binlog.protobuf.BinlogProtos.KeyValue getOptionalParameter(int index);
    int getOptionalParameterCount();
    java.util.List<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
        getOptionalParameterOrBuilderList();
    com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder getOptionalParameterOrBuilder(
        int index);
    
    // optional int32 recordLengthByteLength = 5;
    boolean hasRecordLengthByteLength();
    int getRecordLengthByteLength();
  }
  public static final class BinlogFileHeader extends
      com.google.protobuf.GeneratedMessage
      implements BinlogFileHeaderOrBuilder {
    // Use BinlogFileHeader.newBuilder() to construct.
    private BinlogFileHeader(Builder builder) {
      super(builder);
    }
    private BinlogFileHeader(boolean noInit) {}
    
    private static final BinlogFileHeader defaultInstance;
    public static BinlogFileHeader getDefaultInstance() {
      return defaultInstance;
    }
    
    public BinlogFileHeader getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogFileHeader_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogFileHeader_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional fixed64 createDateTime = 1;
    public static final int CREATEDATETIME_FIELD_NUMBER = 1;
    private long createDateTime_;
    public boolean hasCreateDateTime() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getCreateDateTime() {
      return createDateTime_;
    }
    
    // optional string name = 2;
    public static final int NAME_FIELD_NUMBER = 2;
    private java.lang.Object name_;
    public boolean hasName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          name_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string contentType = 3;
    public static final int CONTENTTYPE_FIELD_NUMBER = 3;
    private java.lang.Object contentType_;
    public boolean hasContentType() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getContentType() {
      java.lang.Object ref = contentType_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          contentType_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getContentTypeBytes() {
      java.lang.Object ref = contentType_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        contentType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // repeated .binlog.KeyValue optionalParameter = 4;
    public static final int OPTIONALPARAMETER_FIELD_NUMBER = 4;
    private java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> optionalParameter_;
    public java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> getOptionalParameterList() {
      return optionalParameter_;
    }
    public java.util.List<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
        getOptionalParameterOrBuilderList() {
      return optionalParameter_;
    }
    public int getOptionalParameterCount() {
      return optionalParameter_.size();
    }
    public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue getOptionalParameter(int index) {
      return optionalParameter_.get(index);
    }
    public com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder getOptionalParameterOrBuilder(
        int index) {
      return optionalParameter_.get(index);
    }
    
    // optional int32 recordLengthByteLength = 5;
    public static final int RECORDLENGTHBYTELENGTH_FIELD_NUMBER = 5;
    private int recordLengthByteLength_;
    public boolean hasRecordLengthByteLength() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getRecordLengthByteLength() {
      return recordLengthByteLength_;
    }
    
    private void initFields() {
      createDateTime_ = 0L;
      name_ = "";
      contentType_ = "";
      optionalParameter_ = java.util.Collections.emptyList();
      recordLengthByteLength_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeFixed64(1, createDateTime_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getNameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getContentTypeBytes());
      }
      for (int i = 0; i < optionalParameter_.size(); i++) {
        output.writeMessage(4, optionalParameter_.get(i));
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(5, recordLengthByteLength_);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFixed64Size(1, createDateTime_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getNameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getContentTypeBytes());
      }
      for (int i = 0; i < optionalParameter_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, optionalParameter_.get(i));
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, recordLengthByteLength_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeaderOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogFileHeader_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogFileHeader_fieldAccessorTable;
      }
      
      // Construct using com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOptionalParameterFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        createDateTime_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        contentType_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        if (optionalParameterBuilder_ == null) {
          optionalParameter_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          optionalParameterBuilder_.clear();
        }
        recordLengthByteLength_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader.getDescriptor();
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader getDefaultInstanceForType() {
        return com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader.getDefaultInstance();
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader build() {
        com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader buildPartial() {
        com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader result = new com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.createDateTime_ = createDateTime_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.contentType_ = contentType_;
        if (optionalParameterBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            optionalParameter_ = java.util.Collections.unmodifiableList(optionalParameter_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.optionalParameter_ = optionalParameter_;
        } else {
          result.optionalParameter_ = optionalParameterBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        result.recordLengthByteLength_ = recordLengthByteLength_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader) {
          return mergeFrom((com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader other) {
        if (other == com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader.getDefaultInstance()) return this;
        if (other.hasCreateDateTime()) {
          setCreateDateTime(other.getCreateDateTime());
        }
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasContentType()) {
          setContentType(other.getContentType());
        }
        if (optionalParameterBuilder_ == null) {
          if (!other.optionalParameter_.isEmpty()) {
            if (optionalParameter_.isEmpty()) {
              optionalParameter_ = other.optionalParameter_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensureOptionalParameterIsMutable();
              optionalParameter_.addAll(other.optionalParameter_);
            }
            onChanged();
          }
        } else {
          if (!other.optionalParameter_.isEmpty()) {
            if (optionalParameterBuilder_.isEmpty()) {
              optionalParameterBuilder_.dispose();
              optionalParameterBuilder_ = null;
              optionalParameter_ = other.optionalParameter_;
              bitField0_ = (bitField0_ & ~0x00000008);
              optionalParameterBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getOptionalParameterFieldBuilder() : null;
            } else {
              optionalParameterBuilder_.addAllMessages(other.optionalParameter_);
            }
          }
        }
        if (other.hasRecordLengthByteLength()) {
          setRecordLengthByteLength(other.getRecordLengthByteLength());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 9: {
              bitField0_ |= 0x00000001;
              createDateTime_ = input.readFixed64();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              name_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              contentType_ = input.readBytes();
              break;
            }
            case 34: {
              com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder subBuilder = com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addOptionalParameter(subBuilder.buildPartial());
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              recordLengthByteLength_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional fixed64 createDateTime = 1;
      private long createDateTime_ ;
      public boolean hasCreateDateTime() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getCreateDateTime() {
        return createDateTime_;
      }
      public Builder setCreateDateTime(long value) {
        bitField0_ |= 0x00000001;
        createDateTime_ = value;
        onChanged();
        return this;
      }
      public Builder clearCreateDateTime() {
        bitField0_ = (bitField0_ & ~0x00000001);
        createDateTime_ = 0L;
        onChanged();
        return this;
      }
      
      // optional string name = 2;
      private java.lang.Object name_ = "";
      public boolean hasName() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setName(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
        return this;
      }
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000002);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      void setName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        name_ = value;
        onChanged();
      }
      
      // optional string contentType = 3;
      private java.lang.Object contentType_ = "";
      public boolean hasContentType() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getContentType() {
        java.lang.Object ref = contentType_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          contentType_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setContentType(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        contentType_ = value;
        onChanged();
        return this;
      }
      public Builder clearContentType() {
        bitField0_ = (bitField0_ & ~0x00000004);
        contentType_ = getDefaultInstance().getContentType();
        onChanged();
        return this;
      }
      void setContentType(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        contentType_ = value;
        onChanged();
      }
      
      // repeated .binlog.KeyValue optionalParameter = 4;
      private java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> optionalParameter_ =
        java.util.Collections.emptyList();
      private void ensureOptionalParameterIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          optionalParameter_ = new java.util.ArrayList<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue>(optionalParameter_);
          bitField0_ |= 0x00000008;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          com.mfizz.binlog.protobuf.BinlogProtos.KeyValue, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder, com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> optionalParameterBuilder_;
      
      public java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> getOptionalParameterList() {
        if (optionalParameterBuilder_ == null) {
          return java.util.Collections.unmodifiableList(optionalParameter_);
        } else {
          return optionalParameterBuilder_.getMessageList();
        }
      }
      public int getOptionalParameterCount() {
        if (optionalParameterBuilder_ == null) {
          return optionalParameter_.size();
        } else {
          return optionalParameterBuilder_.getCount();
        }
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue getOptionalParameter(int index) {
        if (optionalParameterBuilder_ == null) {
          return optionalParameter_.get(index);
        } else {
          return optionalParameterBuilder_.getMessage(index);
        }
      }
      public Builder setOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue value) {
        if (optionalParameterBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureOptionalParameterIsMutable();
          optionalParameter_.set(index, value);
          onChanged();
        } else {
          optionalParameterBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder builderForValue) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.set(index, builderForValue.build());
          onChanged();
        } else {
          optionalParameterBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addOptionalParameter(com.mfizz.binlog.protobuf.BinlogProtos.KeyValue value) {
        if (optionalParameterBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(value);
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue value) {
        if (optionalParameterBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(index, value);
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addOptionalParameter(
          com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder builderForValue) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(builderForValue.build());
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder builderForValue) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(index, builderForValue.build());
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllOptionalParameter(
          java.lang.Iterable<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> values) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          super.addAll(values, optionalParameter_);
          onChanged();
        } else {
          optionalParameterBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearOptionalParameter() {
        if (optionalParameterBuilder_ == null) {
          optionalParameter_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
        } else {
          optionalParameterBuilder_.clear();
        }
        return this;
      }
      public Builder removeOptionalParameter(int index) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.remove(index);
          onChanged();
        } else {
          optionalParameterBuilder_.remove(index);
        }
        return this;
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder getOptionalParameterBuilder(
          int index) {
        return getOptionalParameterFieldBuilder().getBuilder(index);
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder getOptionalParameterOrBuilder(
          int index) {
        if (optionalParameterBuilder_ == null) {
          return optionalParameter_.get(index);  } else {
          return optionalParameterBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
           getOptionalParameterOrBuilderList() {
        if (optionalParameterBuilder_ != null) {
          return optionalParameterBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(optionalParameter_);
        }
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder addOptionalParameterBuilder() {
        return getOptionalParameterFieldBuilder().addBuilder(
            com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.getDefaultInstance());
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder addOptionalParameterBuilder(
          int index) {
        return getOptionalParameterFieldBuilder().addBuilder(
            index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder> 
           getOptionalParameterBuilderList() {
        return getOptionalParameterFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.mfizz.binlog.protobuf.BinlogProtos.KeyValue, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder, com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
          getOptionalParameterFieldBuilder() {
        if (optionalParameterBuilder_ == null) {
          optionalParameterBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.mfizz.binlog.protobuf.BinlogProtos.KeyValue, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder, com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder>(
                  optionalParameter_,
                  ((bitField0_ & 0x00000008) == 0x00000008),
                  getParentForChildren(),
                  isClean());
          optionalParameter_ = null;
        }
        return optionalParameterBuilder_;
      }
      
      // optional int32 recordLengthByteLength = 5;
      private int recordLengthByteLength_ ;
      public boolean hasRecordLengthByteLength() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public int getRecordLengthByteLength() {
        return recordLengthByteLength_;
      }
      public Builder setRecordLengthByteLength(int value) {
        bitField0_ |= 0x00000010;
        recordLengthByteLength_ = value;
        onChanged();
        return this;
      }
      public Builder clearRecordLengthByteLength() {
        bitField0_ = (bitField0_ & ~0x00000010);
        recordLengthByteLength_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:binlog.BinlogFileHeader)
    }
    
    static {
      defaultInstance = new BinlogFileHeader(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:binlog.BinlogFileHeader)
  }
  
  public interface BinlogRecordHeaderOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional int32 id = 1;
    boolean hasId();
    int getId();
    
    // optional fixed64 createDateTime = 2;
    boolean hasCreateDateTime();
    long getCreateDateTime();
    
    // optional int32 userDefinedType = 3;
    boolean hasUserDefinedType();
    int getUserDefinedType();
    
    // repeated .binlog.KeyValue optionalParameter = 4;
    java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> 
        getOptionalParameterList();
    com.mfizz.binlog.protobuf.BinlogProtos.KeyValue getOptionalParameter(int index);
    int getOptionalParameterCount();
    java.util.List<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
        getOptionalParameterOrBuilderList();
    com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder getOptionalParameterOrBuilder(
        int index);
    
    // optional int32 eventId = 5;
    boolean hasEventId();
    int getEventId();
    
    // optional string eventName = 6;
    boolean hasEventName();
    String getEventName();
    
    // optional string eventInfo = 7;
    boolean hasEventInfo();
    String getEventInfo();
  }
  public static final class BinlogRecordHeader extends
      com.google.protobuf.GeneratedMessage
      implements BinlogRecordHeaderOrBuilder {
    // Use BinlogRecordHeader.newBuilder() to construct.
    private BinlogRecordHeader(Builder builder) {
      super(builder);
    }
    private BinlogRecordHeader(boolean noInit) {}
    
    private static final BinlogRecordHeader defaultInstance;
    public static BinlogRecordHeader getDefaultInstance() {
      return defaultInstance;
    }
    
    public BinlogRecordHeader getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogRecordHeader_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogRecordHeader_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional int32 id = 1;
    public static final int ID_FIELD_NUMBER = 1;
    private int id_;
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getId() {
      return id_;
    }
    
    // optional fixed64 createDateTime = 2;
    public static final int CREATEDATETIME_FIELD_NUMBER = 2;
    private long createDateTime_;
    public boolean hasCreateDateTime() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public long getCreateDateTime() {
      return createDateTime_;
    }
    
    // optional int32 userDefinedType = 3;
    public static final int USERDEFINEDTYPE_FIELD_NUMBER = 3;
    private int userDefinedType_;
    public boolean hasUserDefinedType() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getUserDefinedType() {
      return userDefinedType_;
    }
    
    // repeated .binlog.KeyValue optionalParameter = 4;
    public static final int OPTIONALPARAMETER_FIELD_NUMBER = 4;
    private java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> optionalParameter_;
    public java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> getOptionalParameterList() {
      return optionalParameter_;
    }
    public java.util.List<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
        getOptionalParameterOrBuilderList() {
      return optionalParameter_;
    }
    public int getOptionalParameterCount() {
      return optionalParameter_.size();
    }
    public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue getOptionalParameter(int index) {
      return optionalParameter_.get(index);
    }
    public com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder getOptionalParameterOrBuilder(
        int index) {
      return optionalParameter_.get(index);
    }
    
    // optional int32 eventId = 5;
    public static final int EVENTID_FIELD_NUMBER = 5;
    private int eventId_;
    public boolean hasEventId() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getEventId() {
      return eventId_;
    }
    
    // optional string eventName = 6;
    public static final int EVENTNAME_FIELD_NUMBER = 6;
    private java.lang.Object eventName_;
    public boolean hasEventName() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getEventName() {
      java.lang.Object ref = eventName_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          eventName_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getEventNameBytes() {
      java.lang.Object ref = eventName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        eventName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string eventInfo = 7;
    public static final int EVENTINFO_FIELD_NUMBER = 7;
    private java.lang.Object eventInfo_;
    public boolean hasEventInfo() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public String getEventInfo() {
      java.lang.Object ref = eventInfo_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          eventInfo_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getEventInfoBytes() {
      java.lang.Object ref = eventInfo_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        eventInfo_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      id_ = 0;
      createDateTime_ = 0L;
      userDefinedType_ = 0;
      optionalParameter_ = java.util.Collections.emptyList();
      eventId_ = 0;
      eventName_ = "";
      eventInfo_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeFixed64(2, createDateTime_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, userDefinedType_);
      }
      for (int i = 0; i < optionalParameter_.size(); i++) {
        output.writeMessage(4, optionalParameter_.get(i));
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(5, eventId_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(6, getEventNameBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(7, getEventInfoBytes());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFixed64Size(2, createDateTime_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, userDefinedType_);
      }
      for (int i = 0; i < optionalParameter_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, optionalParameter_.get(i));
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, eventId_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, getEventNameBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getEventInfoBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeaderOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogRecordHeader_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.mfizz.binlog.protobuf.BinlogProtos.internal_static_binlog_BinlogRecordHeader_fieldAccessorTable;
      }
      
      // Construct using com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOptionalParameterFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        id_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        createDateTime_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        userDefinedType_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        if (optionalParameterBuilder_ == null) {
          optionalParameter_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          optionalParameterBuilder_.clear();
        }
        eventId_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        eventName_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        eventInfo_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader.getDescriptor();
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader getDefaultInstanceForType() {
        return com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader.getDefaultInstance();
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader build() {
        com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader buildPartial() {
        com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader result = new com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.id_ = id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.createDateTime_ = createDateTime_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.userDefinedType_ = userDefinedType_;
        if (optionalParameterBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            optionalParameter_ = java.util.Collections.unmodifiableList(optionalParameter_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.optionalParameter_ = optionalParameter_;
        } else {
          result.optionalParameter_ = optionalParameterBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        result.eventId_ = eventId_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000010;
        }
        result.eventName_ = eventName_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000020;
        }
        result.eventInfo_ = eventInfo_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader) {
          return mergeFrom((com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader other) {
        if (other == com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasCreateDateTime()) {
          setCreateDateTime(other.getCreateDateTime());
        }
        if (other.hasUserDefinedType()) {
          setUserDefinedType(other.getUserDefinedType());
        }
        if (optionalParameterBuilder_ == null) {
          if (!other.optionalParameter_.isEmpty()) {
            if (optionalParameter_.isEmpty()) {
              optionalParameter_ = other.optionalParameter_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensureOptionalParameterIsMutable();
              optionalParameter_.addAll(other.optionalParameter_);
            }
            onChanged();
          }
        } else {
          if (!other.optionalParameter_.isEmpty()) {
            if (optionalParameterBuilder_.isEmpty()) {
              optionalParameterBuilder_.dispose();
              optionalParameterBuilder_ = null;
              optionalParameter_ = other.optionalParameter_;
              bitField0_ = (bitField0_ & ~0x00000008);
              optionalParameterBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getOptionalParameterFieldBuilder() : null;
            } else {
              optionalParameterBuilder_.addAllMessages(other.optionalParameter_);
            }
          }
        }
        if (other.hasEventId()) {
          setEventId(other.getEventId());
        }
        if (other.hasEventName()) {
          setEventName(other.getEventName());
        }
        if (other.hasEventInfo()) {
          setEventInfo(other.getEventInfo());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              id_ = input.readInt32();
              break;
            }
            case 17: {
              bitField0_ |= 0x00000002;
              createDateTime_ = input.readFixed64();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              userDefinedType_ = input.readInt32();
              break;
            }
            case 34: {
              com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder subBuilder = com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addOptionalParameter(subBuilder.buildPartial());
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              eventId_ = input.readInt32();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              eventName_ = input.readBytes();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              eventInfo_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional int32 id = 1;
      private int id_ ;
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getId() {
        return id_;
      }
      public Builder setId(int value) {
        bitField0_ |= 0x00000001;
        id_ = value;
        onChanged();
        return this;
      }
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0;
        onChanged();
        return this;
      }
      
      // optional fixed64 createDateTime = 2;
      private long createDateTime_ ;
      public boolean hasCreateDateTime() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public long getCreateDateTime() {
        return createDateTime_;
      }
      public Builder setCreateDateTime(long value) {
        bitField0_ |= 0x00000002;
        createDateTime_ = value;
        onChanged();
        return this;
      }
      public Builder clearCreateDateTime() {
        bitField0_ = (bitField0_ & ~0x00000002);
        createDateTime_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int32 userDefinedType = 3;
      private int userDefinedType_ ;
      public boolean hasUserDefinedType() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getUserDefinedType() {
        return userDefinedType_;
      }
      public Builder setUserDefinedType(int value) {
        bitField0_ |= 0x00000004;
        userDefinedType_ = value;
        onChanged();
        return this;
      }
      public Builder clearUserDefinedType() {
        bitField0_ = (bitField0_ & ~0x00000004);
        userDefinedType_ = 0;
        onChanged();
        return this;
      }
      
      // repeated .binlog.KeyValue optionalParameter = 4;
      private java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> optionalParameter_ =
        java.util.Collections.emptyList();
      private void ensureOptionalParameterIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          optionalParameter_ = new java.util.ArrayList<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue>(optionalParameter_);
          bitField0_ |= 0x00000008;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          com.mfizz.binlog.protobuf.BinlogProtos.KeyValue, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder, com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> optionalParameterBuilder_;
      
      public java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> getOptionalParameterList() {
        if (optionalParameterBuilder_ == null) {
          return java.util.Collections.unmodifiableList(optionalParameter_);
        } else {
          return optionalParameterBuilder_.getMessageList();
        }
      }
      public int getOptionalParameterCount() {
        if (optionalParameterBuilder_ == null) {
          return optionalParameter_.size();
        } else {
          return optionalParameterBuilder_.getCount();
        }
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue getOptionalParameter(int index) {
        if (optionalParameterBuilder_ == null) {
          return optionalParameter_.get(index);
        } else {
          return optionalParameterBuilder_.getMessage(index);
        }
      }
      public Builder setOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue value) {
        if (optionalParameterBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureOptionalParameterIsMutable();
          optionalParameter_.set(index, value);
          onChanged();
        } else {
          optionalParameterBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder builderForValue) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.set(index, builderForValue.build());
          onChanged();
        } else {
          optionalParameterBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addOptionalParameter(com.mfizz.binlog.protobuf.BinlogProtos.KeyValue value) {
        if (optionalParameterBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(value);
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue value) {
        if (optionalParameterBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(index, value);
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addOptionalParameter(
          com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder builderForValue) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(builderForValue.build());
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addOptionalParameter(
          int index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder builderForValue) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.add(index, builderForValue.build());
          onChanged();
        } else {
          optionalParameterBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllOptionalParameter(
          java.lang.Iterable<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValue> values) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          super.addAll(values, optionalParameter_);
          onChanged();
        } else {
          optionalParameterBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearOptionalParameter() {
        if (optionalParameterBuilder_ == null) {
          optionalParameter_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
        } else {
          optionalParameterBuilder_.clear();
        }
        return this;
      }
      public Builder removeOptionalParameter(int index) {
        if (optionalParameterBuilder_ == null) {
          ensureOptionalParameterIsMutable();
          optionalParameter_.remove(index);
          onChanged();
        } else {
          optionalParameterBuilder_.remove(index);
        }
        return this;
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder getOptionalParameterBuilder(
          int index) {
        return getOptionalParameterFieldBuilder().getBuilder(index);
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder getOptionalParameterOrBuilder(
          int index) {
        if (optionalParameterBuilder_ == null) {
          return optionalParameter_.get(index);  } else {
          return optionalParameterBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
           getOptionalParameterOrBuilderList() {
        if (optionalParameterBuilder_ != null) {
          return optionalParameterBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(optionalParameter_);
        }
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder addOptionalParameterBuilder() {
        return getOptionalParameterFieldBuilder().addBuilder(
            com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.getDefaultInstance());
      }
      public com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder addOptionalParameterBuilder(
          int index) {
        return getOptionalParameterFieldBuilder().addBuilder(
            index, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.getDefaultInstance());
      }
      public java.util.List<com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder> 
           getOptionalParameterBuilderList() {
        return getOptionalParameterFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.mfizz.binlog.protobuf.BinlogProtos.KeyValue, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder, com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder> 
          getOptionalParameterFieldBuilder() {
        if (optionalParameterBuilder_ == null) {
          optionalParameterBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.mfizz.binlog.protobuf.BinlogProtos.KeyValue, com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder, com.mfizz.binlog.protobuf.BinlogProtos.KeyValueOrBuilder>(
                  optionalParameter_,
                  ((bitField0_ & 0x00000008) == 0x00000008),
                  getParentForChildren(),
                  isClean());
          optionalParameter_ = null;
        }
        return optionalParameterBuilder_;
      }
      
      // optional int32 eventId = 5;
      private int eventId_ ;
      public boolean hasEventId() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public int getEventId() {
        return eventId_;
      }
      public Builder setEventId(int value) {
        bitField0_ |= 0x00000010;
        eventId_ = value;
        onChanged();
        return this;
      }
      public Builder clearEventId() {
        bitField0_ = (bitField0_ & ~0x00000010);
        eventId_ = 0;
        onChanged();
        return this;
      }
      
      // optional string eventName = 6;
      private java.lang.Object eventName_ = "";
      public boolean hasEventName() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public String getEventName() {
        java.lang.Object ref = eventName_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          eventName_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setEventName(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        eventName_ = value;
        onChanged();
        return this;
      }
      public Builder clearEventName() {
        bitField0_ = (bitField0_ & ~0x00000020);
        eventName_ = getDefaultInstance().getEventName();
        onChanged();
        return this;
      }
      void setEventName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000020;
        eventName_ = value;
        onChanged();
      }
      
      // optional string eventInfo = 7;
      private java.lang.Object eventInfo_ = "";
      public boolean hasEventInfo() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public String getEventInfo() {
        java.lang.Object ref = eventInfo_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          eventInfo_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setEventInfo(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        eventInfo_ = value;
        onChanged();
        return this;
      }
      public Builder clearEventInfo() {
        bitField0_ = (bitField0_ & ~0x00000040);
        eventInfo_ = getDefaultInstance().getEventInfo();
        onChanged();
        return this;
      }
      void setEventInfo(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000040;
        eventInfo_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:binlog.BinlogRecordHeader)
    }
    
    static {
      defaultInstance = new BinlogRecordHeader(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:binlog.BinlogRecordHeader)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_binlog_KeyValue_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_binlog_KeyValue_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_binlog_BinlogFileHeader_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_binlog_BinlogFileHeader_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_binlog_BinlogRecordHeader_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_binlog_BinlogRecordHeader_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020idl/binlog.proto\022\006binlog\"&\n\010KeyValue\022\013" +
      "\n\003key\030\001 \001(\t\022\r\n\005value\030\002 \001(\t\"\232\001\n\020BinlogFil" +
      "eHeader\022\026\n\016createDateTime\030\001 \001(\006\022\014\n\004name\030" +
      "\002 \001(\t\022\023\n\013contentType\030\003 \001(\t\022+\n\021optionalPa" +
      "rameter\030\004 \003(\0132\020.binlog.KeyValue\022\036\n\026recor" +
      "dLengthByteLength\030\005 \001(\005\"\265\001\n\022BinlogRecord" +
      "Header\022\n\n\002id\030\001 \001(\005\022\026\n\016createDateTime\030\002 \001" +
      "(\006\022\027\n\017userDefinedType\030\003 \001(\005\022+\n\021optionalP" +
      "arameter\030\004 \003(\0132\020.binlog.KeyValue\022\017\n\007even" +
      "tId\030\005 \001(\005\022\021\n\teventName\030\006 \001(\t\022\021\n\teventInf",
      "o\030\007 \001(\tB)\n\031com.mfizz.binlog.protobufB\014Bi" +
      "nlogProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_binlog_KeyValue_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_binlog_KeyValue_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_binlog_KeyValue_descriptor,
              new java.lang.String[] { "Key", "Value", },
              com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.class,
              com.mfizz.binlog.protobuf.BinlogProtos.KeyValue.Builder.class);
          internal_static_binlog_BinlogFileHeader_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_binlog_BinlogFileHeader_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_binlog_BinlogFileHeader_descriptor,
              new java.lang.String[] { "CreateDateTime", "Name", "ContentType", "OptionalParameter", "RecordLengthByteLength", },
              com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader.class,
              com.mfizz.binlog.protobuf.BinlogProtos.BinlogFileHeader.Builder.class);
          internal_static_binlog_BinlogRecordHeader_descriptor =
            getDescriptor().getMessageTypes().get(2);
          internal_static_binlog_BinlogRecordHeader_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_binlog_BinlogRecordHeader_descriptor,
              new java.lang.String[] { "Id", "CreateDateTime", "UserDefinedType", "OptionalParameter", "EventId", "EventName", "EventInfo", },
              com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader.class,
              com.mfizz.binlog.protobuf.BinlogProtos.BinlogRecordHeader.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
