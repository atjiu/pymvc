<#include "./layout.ftl"/>
<@html>
<p>User list page</p>

<ul>
  <#list users as user>
    <li>${user}</li>
  </#list>
</ul>
</@html>
