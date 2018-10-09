[#ftl]
[#-- @implicitly included --]
[#-- @ftlroot "../../../templates/" --]
[#-- @ftlvariable name="null" type="java.lang.Object" --]
[#-- @ftlvariable name="base_url" type="java.lang.String" --]
[#-- @ftlvariable name="base_path" type="java.lang.String" --]
[#-- @ftlvariable name="remote_base_url" type="java.lang.String" --]
[#-- @ftlvariable name="system_name" type="java.lang.String" --]
[#-- @ftlvariable name="system_version" type="java.lang.String" --]
[#-- @ftlvariable name="system_title" type="java.lang.String" --]
[#-- @ftlvariable name="os_name" type="java.lang.String" --]
[#-- @ftlvariable name="os_version" type="java.lang.String" --]
[#-- @ftlvariable name="os_arth" type="java.lang.String" --]
[#-- @ftlvariable name="user_name" type="java.lang.String" --]
[#-- @ftlvariable name="host_name" type="java.lang.String" --]
[#-- @ftlvariable name="host_address" type="java.util.List<java.lang.String>" --]
[#-- @ftlvariable name="java_version" type="java.lang.String" --]
[#-- @ftlvariable name="springboot_version" type="java.lang.String" --]
[#-- @ftlvariable name="tomcat_version" type="java.lang.String" --]
[#-- @ftlvariable name="servlet_version" type="java.lang.String" --]
[#-- @ftlvariable name="mysql_version" type="java.lang.String" --]
[#-- @ftlvariable name="hibernate_version" type="java.lang.String" --]
[#-- @ftlvariable name="poi_version" type="java.lang.String" --]
[#-- @ftlvariable name="is_release" type="java.lang.Boolean" --]
[#-- @ftlvariable name="logger" type="org.slf4j.Logger" --]
[#-- @ftlvariable name="request" type="javax.servlet.http.HttpServletRequest" --]
[#-- @ftlvariable name="response" type="javax.servlet.http.HttpServletResponse" --]
[#-- @ftlvariable name="session" type="javax.servlet.http.HttpSession" --]
[#-- @ftlvariable name="application" type="javax.servlet.ServletContext" --]
[#macro json_dump obj][/#macro]
[#macro shuffle_strategy_loop shuffle_strategy_setup elements][/#macro]
[#macro compress][/#macro]
[#function url_yaml_parse url][/#function]
[#function class_path_yaml_parse class_path][/#function]
[#function file_system_yaml_parse file_system][/#function]
[#function string_yaml_parse str][/#function]
[#function get_dict_property class_name][/#function]
[#function abbreviate str width ellipsis][/#function]
[#function constant var][/#function]
[#function current_user][/#function]
[#function current_library][/#function]
[#function all_libraries][/#function]
[#include '/view/includes/main.ftl'/]
[#include '/doc/common/arrangement.ftl'/]

[#-- for Word --]
[#-- @ftlvariable name="chinese_font_family" type="java.lang.String" --]
[#-- @ftlvariable name="english_font_family" type="java.lang.String" --]
[#-- @ftlvariable name="font_size" type="java.lang.String" --]
[#-- @ftlvariable name="line_height" type="java.lang.String" --]
[#-- @ftlvariable name="separate_columns" type="java.lang.String" --]
[#-- @ftlvariable name="show_separators" type="java.lang.Boolean" --]
[#-- @ftlvariable name="insert_blank_line_between_words" type="java.lang.Boolean" --]
[#-- @ftlvariable name="definition_arranges_vertically" type="java.lang.Boolean" --]
[#-- @ftlvariable name="table_border_color" type="java.lang.String" --]