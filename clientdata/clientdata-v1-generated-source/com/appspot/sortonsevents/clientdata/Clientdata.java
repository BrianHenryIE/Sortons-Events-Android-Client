/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-10-30 15:57:41 UTC)
 * on 2013-11-13 at 20:45:23 UTC 
 * Modify at your own risk.
 */

package com.appspot.sortonsevents.clientdata;

/**
 * Service definition for Clientdata (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link ClientdataRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Clientdata extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.16.0-rc of the clientdata library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://sortonsevents.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "clientdata/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Clientdata(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Clientdata(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * An accessor for creating requests from the ClientPageDataEndpoint collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Clientdata clientdata = new Clientdata(...);}
   *   {@code Clientdata.ClientPageDataEndpoint.List request = clientdata.clientPageDataEndpoint().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public ClientPageDataEndpoint clientPageDataEndpoint() {
    return new ClientPageDataEndpoint();
  }

  /**
   * The "clientPageDataEndpoint" collection of methods.
   */
  public class ClientPageDataEndpoint {

    /**
     * Create a request for the method "clientPageDataEndpoint.getClientPageData".
     *
     * This request holds the parameters needed by the the clientdata server.  After setting any
     * optional parameters, call the {@link GetClientPageData#execute()} method to invoke the remote
     * operation.
     *
     * @param clientid
     * @return the request
     */
    public GetClientPageData getClientPageData(java.lang.String clientid) throws java.io.IOException {
      GetClientPageData result = new GetClientPageData(clientid);
      initialize(result);
      return result;
    }

    public class GetClientPageData extends ClientdataRequest<com.appspot.sortonsevents.clientdata.model.ClientPageData> {

      private static final String REST_PATH = "clientpagedata/{clientid}";

      /**
       * Create a request for the method "clientPageDataEndpoint.getClientPageData".
       *
       * This request holds the parameters needed by the the clientdata server.  After setting any
       * optional parameters, call the {@link GetClientPageData#execute()} method to invoke the remote
       * operation. <p> {@link GetClientPageData#initialize(com.google.api.client.googleapis.services.Ab
       * stractGoogleClientRequest)} must be called to initialize this instance immediately after
       * invoking the constructor. </p>
       *
       * @param clientid
       * @since 1.13
       */
      protected GetClientPageData(java.lang.String clientid) {
        super(Clientdata.this, "GET", REST_PATH, null, com.appspot.sortonsevents.clientdata.model.ClientPageData.class);
        this.clientid = com.google.api.client.util.Preconditions.checkNotNull(clientid, "Required parameter clientid must be specified.");
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public GetClientPageData setAlt(java.lang.String alt) {
        return (GetClientPageData) super.setAlt(alt);
      }

      @Override
      public GetClientPageData setFields(java.lang.String fields) {
        return (GetClientPageData) super.setFields(fields);
      }

      @Override
      public GetClientPageData setKey(java.lang.String key) {
        return (GetClientPageData) super.setKey(key);
      }

      @Override
      public GetClientPageData setOauthToken(java.lang.String oauthToken) {
        return (GetClientPageData) super.setOauthToken(oauthToken);
      }

      @Override
      public GetClientPageData setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (GetClientPageData) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public GetClientPageData setQuotaUser(java.lang.String quotaUser) {
        return (GetClientPageData) super.setQuotaUser(quotaUser);
      }

      @Override
      public GetClientPageData setUserIp(java.lang.String userIp) {
        return (GetClientPageData) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String clientid;

      /**

       */
      public java.lang.String getClientid() {
        return clientid;
      }

      public GetClientPageData setClientid(java.lang.String clientid) {
        this.clientid = clientid;
        return this;
      }

      @Override
      public GetClientPageData set(String parameterName, Object value) {
        return (GetClientPageData) super.set(parameterName, value);
      }
    }

  }

  /**
   * An accessor for creating requests from the ClientdataOperations collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Clientdata clientdata = new Clientdata(...);}
   *   {@code Clientdata.ClientdataOperations.List request = clientdata.clientdata().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public ClientdataOperations clientdata() {
    return new ClientdataOperations();
  }

  /**
   * The "clientdata" collection of methods.
   */
  public class ClientdataOperations {

    /**
     * Create a request for the method "clientdata.addPage".
     *
     * This request holds the parameters needed by the the clientdata server.  After setting any
     * optional parameters, call the {@link AddPage#execute()} method to invoke the remote operation.
     *
     * @param clientpageid
     * @param content the {@link com.appspot.sortonsevents.clientdata.model.FbPage}
     * @return the request
     */
    public AddPage addPage(java.lang.String clientpageid, com.appspot.sortonsevents.clientdata.model.FbPage content) throws java.io.IOException {
      AddPage result = new AddPage(clientpageid, content);
      initialize(result);
      return result;
    }

    public class AddPage extends ClientdataRequest<com.appspot.sortonsevents.clientdata.model.FbPage> {

      private static final String REST_PATH = "addPage/{clientpageid}";

      /**
       * Create a request for the method "clientdata.addPage".
       *
       * This request holds the parameters needed by the the clientdata server.  After setting any
       * optional parameters, call the {@link AddPage#execute()} method to invoke the remote operation.
       * <p> {@link
       * AddPage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
       * be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param clientpageid
       * @param content the {@link com.appspot.sortonsevents.clientdata.model.FbPage}
       * @since 1.13
       */
      protected AddPage(java.lang.String clientpageid, com.appspot.sortonsevents.clientdata.model.FbPage content) {
        super(Clientdata.this, "POST", REST_PATH, content, com.appspot.sortonsevents.clientdata.model.FbPage.class);
        this.clientpageid = com.google.api.client.util.Preconditions.checkNotNull(clientpageid, "Required parameter clientpageid must be specified.");
      }

      @Override
      public AddPage setAlt(java.lang.String alt) {
        return (AddPage) super.setAlt(alt);
      }

      @Override
      public AddPage setFields(java.lang.String fields) {
        return (AddPage) super.setFields(fields);
      }

      @Override
      public AddPage setKey(java.lang.String key) {
        return (AddPage) super.setKey(key);
      }

      @Override
      public AddPage setOauthToken(java.lang.String oauthToken) {
        return (AddPage) super.setOauthToken(oauthToken);
      }

      @Override
      public AddPage setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (AddPage) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public AddPage setQuotaUser(java.lang.String quotaUser) {
        return (AddPage) super.setQuotaUser(quotaUser);
      }

      @Override
      public AddPage setUserIp(java.lang.String userIp) {
        return (AddPage) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String clientpageid;

      /**

       */
      public java.lang.String getClientpageid() {
        return clientpageid;
      }

      public AddPage setClientpageid(java.lang.String clientpageid) {
        this.clientpageid = clientpageid;
        return this;
      }

      @Override
      public AddPage set(String parameterName, Object value) {
        return (AddPage) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "clientdata.ignorePage".
     *
     * This request holds the parameters needed by the the clientdata server.  After setting any
     * optional parameters, call the {@link IgnorePage#execute()} method to invoke the remote operation.
     *
     * @param clientpageid
     * @param content the {@link com.appspot.sortonsevents.clientdata.model.FbPage}
     * @return the request
     */
    public IgnorePage ignorePage(java.lang.String clientpageid, com.appspot.sortonsevents.clientdata.model.FbPage content) throws java.io.IOException {
      IgnorePage result = new IgnorePage(clientpageid, content);
      initialize(result);
      return result;
    }

    public class IgnorePage extends ClientdataRequest<com.appspot.sortonsevents.clientdata.model.FbPage> {

      private static final String REST_PATH = "ignorePage/{clientpageid}";

      /**
       * Create a request for the method "clientdata.ignorePage".
       *
       * This request holds the parameters needed by the the clientdata server.  After setting any
       * optional parameters, call the {@link IgnorePage#execute()} method to invoke the remote
       * operation. <p> {@link
       * IgnorePage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
       * must be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param clientpageid
       * @param content the {@link com.appspot.sortonsevents.clientdata.model.FbPage}
       * @since 1.13
       */
      protected IgnorePage(java.lang.String clientpageid, com.appspot.sortonsevents.clientdata.model.FbPage content) {
        super(Clientdata.this, "POST", REST_PATH, content, com.appspot.sortonsevents.clientdata.model.FbPage.class);
        this.clientpageid = com.google.api.client.util.Preconditions.checkNotNull(clientpageid, "Required parameter clientpageid must be specified.");
      }

      @Override
      public IgnorePage setAlt(java.lang.String alt) {
        return (IgnorePage) super.setAlt(alt);
      }

      @Override
      public IgnorePage setFields(java.lang.String fields) {
        return (IgnorePage) super.setFields(fields);
      }

      @Override
      public IgnorePage setKey(java.lang.String key) {
        return (IgnorePage) super.setKey(key);
      }

      @Override
      public IgnorePage setOauthToken(java.lang.String oauthToken) {
        return (IgnorePage) super.setOauthToken(oauthToken);
      }

      @Override
      public IgnorePage setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (IgnorePage) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public IgnorePage setQuotaUser(java.lang.String quotaUser) {
        return (IgnorePage) super.setQuotaUser(quotaUser);
      }

      @Override
      public IgnorePage setUserIp(java.lang.String userIp) {
        return (IgnorePage) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String clientpageid;

      /**

       */
      public java.lang.String getClientpageid() {
        return clientpageid;
      }

      public IgnorePage setClientpageid(java.lang.String clientpageid) {
        this.clientpageid = clientpageid;
        return this;
      }

      @Override
      public IgnorePage set(String parameterName, Object value) {
        return (IgnorePage) super.set(parameterName, value);
      }
    }

  }

  /**
   * Builder for {@link Clientdata}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Clientdata}. */
    @Override
    public Clientdata build() {
      return new Clientdata(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link ClientdataRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setClientdataRequestInitializer(
        ClientdataRequestInitializer clientdataRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(clientdataRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
