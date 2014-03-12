/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package at.kc.tugraz.ss.adapter.websocket.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

@WebServlet (name="ss-adapter-websocket", displayName="ss-adapter-websocket", urlPatterns = {"/ss-adapter-websocket"}, loadOnStartup=1)
public class SSInterfaceWebSocket extends WebSocketServlet {

  private static final long serialVersionUID = 1L;

  public SSInterfaceWebSocket() {
    super();
  }

  @Override
  protected StreamInbound createWebSocketInbound(String string, HttpServletRequest hsr) {
    
    SSInterfaceWebSocketHandler ssInterfaceWebSocketHandler = null;
    
    try{
      ssInterfaceWebSocketHandler = new SSInterfaceWebSocketHandler();
    }catch(Exception error){
    }
    
    return ssInterfaceWebSocketHandler;
  }
}