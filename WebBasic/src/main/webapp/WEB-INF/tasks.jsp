<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 03.04.2023
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="itstep.learning.data.entity.Task" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="itstep.learning.data.entity.Story" %>
<%@ page import="itstep.learning.data.entity.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String domain = request.getContextPath();
    User authUser = (User) request.getSession().getAttribute("authUser");
    List<Team> teams = (List<Team>) request.getAttribute("teams");
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    List<Story> stories = (List<Story>) request.getAttribute("stories");
    Collections.sort( tasks, new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o2.getPriority() - o1.getPriority();
        }
    });
    String[] priority_colors = {
            "green-text text-lighten-1",
            "yellow-text text-lighten-1",
            "red-text text-lighten-2"
    };
    String[] priority_levels = {"Low", "Normal", "High"};
%>

<div class="row">
    <div class="col s5 m4 l3">
        <%
            for (Task task : tasks ) {
                int p = task.getPriority();
        %>
        <div>
<%--            fiber_manual_record--%>
            <div>
                <div class="card blue-grey darken-1">
                    <div class="card-content white-text">
                        <span id="<%= task.getId() %>" class="card-title"><%= task.getName() %></span>
                        <p><b> Id: </b><span class="grey-text text-lighten-2"><%= task.getId() %></span>
                        </p>
                        <p><b>Deadline: </b><span class="grey-text text-lighten-2"><%= task.getDeadline() %></span>
                        </p>
                        <b>Priority: <span class="<%= priority_colors[p]%>">
                    <%= priority_levels[p]%>
                </span></b>
                    </div>
                    <div class="card-action">
                        <a style="cursor: pointer; user-select: none;"
                           class="choose-link"  data-taskId="<%= task.getId() %>">choose</a>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>
    <div class="col s7 m8 l9">
        <div id="chat">
        </div>
        <form id="story-form" class="row" method="post">
            <textarea id="story-textarea" class="materialize-textarea" name="story-text"></textarea>
            <label for="story-textarea">Textarea</label>
            <div class="row input-field right-align">
                <b>Task:</b>
                <div style="margin: 0;" id="chip-task-name" class="chip">
                    NONE
                </div>
                <button class="btn waves-effect waves-teal" type="submit">SEND<i class="material-icons right">send</i>
                </button>
            </div>
            <input type="hidden" name="story-id-task"/>
            <input type="hidden" name="story-id-reply"/>
        </form>
    </div>
</div>

<%-- Add TaskEntity --%>
<div class="row">
    <h4>Add Task</h4>
    <form id="task-form" class="col s10 offset-s1 m8 offset-m2 l6 offset-l3" method="post">
        <div class="row input-field"><i class="material-icons prefix">content_paste</i>
            <input id="task-name" type="text" name="task-name"/>
            <label for="task-name">Title</label>
        </div>
        <div class="row input-field"><i class="material-icons prefix">people_outline</i>
            <select name="task-team">
                <option value="" disabled selected>Select command</option>
                <% for (Team team : teams ) { %>
                <option value="<%=team.getId()%>"><%=team.getName()%>
                </option>
                <% } %>
            </select>
            <label>Command</label>
        </div>
        <div class="row input-field"><i class="material-icons prefix">priority_high</i>
            <select name="task-priority">
                <option value="" disabled selected>Select priority</option>
                <option value="0">Low</option>
                <option value="1">Normal</option>
                <option value="2">High</option>
            </select>
            <label>Command</label>
        </div>
        <div class="row input-field"><i class="material-icons prefix">event_available</i>
            <input id="task-deadline" type="text" class="datepicker" name="task-deadline"/>
            <label for="task-deadline">Completion</label>
        </div>
        <div class="row input-field right-align">
            <button class="btn waves-effect waves-teal" type="submit">ADD<i class="material-icons right">add</i>
            </button>
        </div>
    </form>
</div>

<script>

    const tpl = "<div style='margin-top: 10px'><i>{{moment}}</i>&emsp;<span>to {{task}}</span></br><b>{{author}}: </b>&emsp;" +
        "<span>{{content}}</span></div>";

    document.addEventListener("DOMContentLoaded", function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems, {});
        elems = document.querySelectorAll('.datepicker');
        instances = M.Datepicker.init(elems, { format: "yyyy-mm-dd" });
        elems = document.querySelectorAll('.chips');
        instances = M.Chips.init(elems, null);
        let chooseElems = document.getElementsByClassName("choose-link");
        for ( let i = 0; i < chooseElems.length; i++ ) {
            chooseElems.item(i).onclick = chooseLinkClick;
        }

        initWebsocket() ;
    });

    document.addEventListener("submit", e => {
        e.preventDefault();
        switch (e.target.id) {
            case 'story-form' :
                sendStoryForm();
                break;
            case 'task-form' :
                sendTaskForm();
                break;
        }
    });

    function initWebsocket() {
        window.websocket = new WebSocket( `ws://${window.location.host}/WebBasic/chat` );
        window.websocket.onopen = onWsOpen;
        window.websocket.onmessage = onWsMessage;
        window.websocket.onclose = onWsClose;
        window.websocket.onerror = onWsError;

    }

    function onWsOpen( message ) {
        console.log( "onWsOpen", message );
    }

    function onWsMessage( message ) {
        const chat = document.getElementById("chat");
        let model = JSON.parse( message.data );
        if( typeof model.status !== "undefined" ) {
            M.toast({html: "Message was not send"});
        } else {
            let taskId = document.querySelector('input[name="story-id-task"]').value;
            let modelTaskId = model.task.id;
            let pointer = `<i class="material-icons red-text right">fiber_manual_record</i>`;
            if( modelTaskId === taskId ) {
                chat.innerHTML += htmlFromStoryModel( model );
            } else if( modelTaskId !== undefined ) {
                let taskElement = document.getElementById( modelTaskId );
                if( !taskElement.innerHTML.endsWith( pointer ) ) {
                    taskElement.innerHTML +=
                        pointer;
                }
            }
        }
    }

    function onWsClose( message ) {
        console.log( "onWsClose", message );
    }
    function onWsError( message ) {
        console.log( "onWsError", message );
    }

    function sendStoryForm() {
        if( ! window.websocket ) throw 'websocket not init';
        const storyIdTask = document.querySelector('input[name="story-id-task"]');
        if( ! storyIdTask ) throw 'input[name="story-id-task"] not found';
        const taskId = storyIdTask.value;

        const textarea =  document.getElementById("story-textarea");
        if( ! textarea ) throw 'story-textarea not found';
        const message = textarea.value;

        if( taskId !== "" ) {
            window.websocket.send(
                JSON.stringify({
                    taskId: taskId,
                    content: message
                })
            );
            textarea.value = "";
        } else {
            M.toast({html: "Select task"});
        }
    }

    function sendStoryFormHttp() {
        fetch('<%= domain + "/story" %>', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams(new FormData(document.querySelector("#story-form")))
        }).then(r => r.text())
            .then(t => {
                if (t === "OK") {
                    t = "story added";
                    window.location.reload();
                }
                M.toast({html: t});
            });
    }

    function sendTaskForm() {
        fetch('<%= domain + "/home" %>', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams(new FormData(document.querySelector("#task-form")))
        }).then(r => r.text())
            .then(t => {
                if (t === "OK") {
                    t = "task added";
                    window.location.reload();
                }
                M.toast({html: t});
            });
    }
    function chooseLinkClick(e) {
        const taskId = e.target.dataset.taskid ;
        if (!/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/.test(taskId)) return;
        document.querySelector('input[name="story-id-task"]').value = taskId;
        document.getElementById("chip-task-name").innerHTML = taskId;
        const taskElement = document.getElementById( taskId );
        let pointer = `<i class="material-icons red-text right">fiber_manual_record</i>`;
        taskElement.innerHTML = taskElement.innerHTML.replace(
            pointer,
            ''
        );
        const chat = document.getElementById("chat");
        fetch(`<%= domain %>/story?taskId=${taskId}`, {
            method: "GET",
            headers: {
                'Content-Type': '*/*'
            }
        }).then( r => r.json() )
            .then( j => {
                if( typeof j.status !== "undefined" ) {
                    chat.innerHTML = "<h3 style='text-align: center'>No stories</h3>";
                } else {
                    let len = j.length;
                    let chatHtml = "";
                    const models = j.sort(compareModelsByDates);
                    for ( let i = 0; i < len; i++ ) {
                        const model = models[i];
                        chatHtml += htmlFromStoryModel( model );
                    }
                    chat.innerHTML = chatHtml;
                }
            });
        window.scrollTo(0, 0);
        // M.toast({html: e});
    }

    const compareModelsByDates = (a, b) => {
        const aDate = new Date( Date.parse( a.createdDate ) );
        const bDate = new Date( Date.parse( b.createdDate ) );
        if( aDate < bDate ) {
            return -1;
        }
        if ( aDate > bDate ) {
            return 1;
        }
        return 0;
    }

    function htmlFromStoryModel( model ) {
        return tpl
            .replace("{{moment}}", toModelDateString( model.createdDate ) )
            .replace("{{author}}", model.author )
            .replace("{{content}}", model.content )
            .replace("{{task}}", model.task.name )
    }

    function toModelDateString( modelDate ) {
        const cr_dt = new Date( Date.parse( modelDate ) );
        const new_dt = new Date();

        return cr_dt.toLocaleDateString() === new_dt.toLocaleDateString()
            ? cr_dt.toLocaleTimeString()
            : cr_dt.toLocaleString();
    }

    function todayDates(d1, d2) {
        let date1 = new Date(d1).getDate();
        let date2 = new Date(d2).getDate();

        if (date1 < date2) {
            return false;
        } else if (date1 > date2) {
            return true;
        }
    }
</script>
