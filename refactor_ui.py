import os
import re
from bs4 import BeautifulSoup
import glob

base_dir = r"c:\Users\r3384\Documents\workspace-spring-tools-for-eclipse-4.30.0.RELEASE\Ecommers-1\src\main\resources\templates"

new_css = """
        /* NAVBAR CSS REPLACING SIDEBAR */
        .navbar {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-bottom: 2px solid var(--bdr);
            padding: 12px 40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            position: sticky;
            top: 0;
            z-index: 100;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
        }

        .navbar-links {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .nav-link {
            display: flex;
            align-items: center;
            gap: 8px;
            color: var(--t);
            text-decoration: none;
            padding: 10px 16px;
            border-radius: 12px;
            font-size: 14px;
            font-weight: 700;
            transition: all 0.2s;
            cursor: pointer;
        }

        .nav-link:hover {
            background: var(--bg);
            color: var(--b);
            transform: translateY(-2px);
        }

        .nav-link.active {
            background: var(--y);
            color: var(--b);
            box-shadow: 3px 3px 0 var(--b);
            border: 2px solid var(--b);
        }

        /* MAIN */
        .main {
            flex: 1;
            /* margin-left removed */
            display: flex;
            flex-direction: column;
        }
        
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background: var(--bg);
            color: var(--b);
            display: flex;
            flex-direction: column; /* Changed from row */
            min-height: 100vh;
        }
        
        .brand {
            font-size: 24px;
            font-weight: 900;
            margin: 0;
            padding: 0;
            border: none;
        }
        
        .brand span {
            background: var(--y);
            padding: 2px 8px;
            border-radius: 6px;
            color: var(--b);
        }
"""

def process_html_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # If it doesn't have sidebar, skip
    if 'class="sidebar"' not in content:
        return

    # Replace CSS
    # Remove old sidebar and main css
    css_to_remove = [
        r"\.sidebar\s*\{[^}]*\}",
        r"\.brand\s*\{[^}]*\}",
        r"\.brand span\s*\{[^}]*\}",
        r"\.nav-sec\s*\{[^}]*\}",
        r"\.nav-link\s*\{[^}]*\}",
        r"\.nav-link:hover\s*\{[^}]*\}",
        r"\.nav-link\.active\s*\{[^}]*\}",
        r"\.nav-link i\s*\{[^}]*\}",
        r"\.sidebar-foot\s*\{[^}]*\}",
        r"\.main\s*\{[^}]*\}",
        r"body\s*\{[^}]*\}"
    ]
    
    for pattern in css_to_remove:
        content = re.sub(pattern, "", content, flags=re.DOTALL)
        
    # Insert new CSS just before </style>
    content = content.replace("</style>", new_css + "\n    </style>")

    # Parse HTML with BeautifulSoup to transform the DOM
    soup = BeautifulSoup(content, 'html.parser')
    
    sidebar = soup.find('div', class_='sidebar')
    if sidebar:
        navbar = soup.new_tag('nav', **{'class': 'navbar'})
        
        brand = sidebar.find('div', class_='brand')
        if brand:
            navbar.append(brand)
            
        nav_links_div = soup.new_tag('div', **{'class': 'navbar-links'})
        
        # Grab all nav-links
        nav_links = sidebar.find_all(class_='nav-link')
        for link in nav_links:
            # If it's the logout link, separate it
            if 'logout' in link.get('href', '').lower() or 'Sign Out' in link.text:
                continue
            nav_links_div.append(link)
            
        navbar.append(nav_links_div)
        
        # Actions div
        actions_div = soup.new_tag('div', **{'class': 'navbar-actions', 'style': 'display:flex; gap:16px; align-items:center;'})
        
        # See if there's a topbar badge-role we can move
        main_div = soup.find('div', class_='main')
        if main_div:
            topbar = main_div.find('div', class_='topbar')
            if topbar:
                badge = topbar.find(class_='badge-role')
                if badge:
                    actions_div.append(badge)
                # Remove topbar since it's redundant now? Or keep it for page title.
                # Let's keep topbar but remove the badge-role from it.
        
        logout_link = sidebar.find(lambda tag: tag.has_attr('class') and 'nav-link' in tag['class'] and ('logout' in tag.get('href', '').lower() or 'Sign Out' in tag.text))
        if logout_link:
            logout_link['style'] = "color:var(--r); border:2px solid var(--r); background:transparent;"
            actions_div.append(logout_link)
            
        navbar.append(actions_div)
        
        # Replace sidebar with navbar
        sidebar.replace_with(navbar)
        
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(str(soup))
        print(f"Updated {filepath}")

for root, dirs, files in os.walk(base_dir):
    for name in files:
        if name.endswith(".html"):
            process_html_file(os.path.join(root, name))

print("Done compiling all UI files.")
