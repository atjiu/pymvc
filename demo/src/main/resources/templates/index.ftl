<#include "./layout.ftl"/>
<@html>
    <p>Index page</p>

    <p>Hello, <span style="color: #ff0000;">${name!}</span> - age: ${age!} - gender: <#if gender>man<#else>woman</#if> - salary: ${salary!}</p>
</@html>
