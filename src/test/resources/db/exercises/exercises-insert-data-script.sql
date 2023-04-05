INSERT INTO programs(title, date)
VALUES ('Программа Людмила Пименова', '2022-05-16 00:00');

INSERT INTO therapeutic_purposes(purpose)
VALUES ('Снятие гипертонуса с мышц шеи');

INSERT INTO purpose_programs(purpose_id, program_id)
VALUES (1, 1);

INSERT INTO therapists(user_id, name)
VALUES (1, 'Иванов И.И.'),
       (2, 'Екатерина Маркова');

INSERT INTO exercises(title, description, indications, contradictions, duration, exercise_type_id, therapist_id)
VALUES ('Разминка для шеи', '', '', '', '00:10:00', 1, 1),
       ('ПИР на верхние трапеции', '', '', '', '00:10:00', 4, 1),
       ('ПИР на ременные мышцы шеи и головы', '', '', '', '00:10:00', 4, 1),
       ('Укрепление глубоких сгибателей шеи', '', '', '', '00:04:00', 3, 1),
       ('Тракция и расслабление шеи в шавасане', '', '', '', '00:15:00', 6, 1);

INSERT INTO exercise_purposes(exercise_id, purpose_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1);

INSERT INTO exercise_steps(description, image_id, exercise_id)
VALUES ('Встаньте ровно, прижмите пятки к полу, потянитесь макушкой к потолку, почувствуйте как вытягивается позвоночник.',
        null, 1),
       ('На выдохе поворот головы в сторону, на вдохе возврат головы в центр, на следующем выдохе - голова в другую сторону. Прислушивайтесь к ощущениям в мышцах, сравнивайте ощущения справа и слева. По 5 поворотов головы в каждую сторону.',
        null, 1),
       ('На выдохе наклон головы к плечу, на вдохе вытяжение макушки к потолку, на следующем выдохе- наклон к другому плечу. Прислушивайтесь к ощущениям в мышцах, сравнивайте ощущения справа и слева. По 5 наклонов головы в каждую сторону.',
        null, 1),
       ('Положите обе ладони под яремную ямку и выполняйте медленные осознанные вращения головой, после каждого круга меняйте направление вращения. По 3 круга в каждом направлении.',
        null, 1),
       ('Разомните массажным мячиком возле стены верхние трапеции и мышцы между лопаток.', null, 1),
       ('Убедитесь, что ваша спина прямая, подбородок направлен назад и вверх.', null, 2),
       ('Положите правую руку на правый висок. На вдохе рука давит на висок, шея сопротивляется (в этот момент сокращается верхняя трапеция справа).',
        null, 2),
       ('На выдохе голову наклоните к левому плечу, левую ладонь положите на правый висок. Правое плечо направляйте вниз. Прислушивайтесь к ощущениям вытяжения в трапеции. Повторите по 3-4 раза в каждую сторону.',
        null, 2),
       ('Если есть асимметрия в тонусе верхних трапеций, то на ту сторону, где трапеция более зажата, сделайте на 1 подход больше.',
        null, 2),
       ('Убедитесь, что ваша спина прямая, подбородок направлен назад и вверх.', null, 3),
       ('Наклоните лицо к полу. Из этого положения наклоните голову к правому плечу. Положите правую ладонь на левый затылочный бугор. Нос и правый локоть направлены в диагональ.',
        null, 3),
       ('На вдохе голова пытается вернутся в нейтральную позицию, рука не дает. На выдохе - шея расслабляется и рука создает легкое давление, усиливая вытяжение по задней и боковой поверхности шеи. Повторить по 3-4 раза в каждую сторону.',
        null, 3),
       ('Если есть асимметрия в тонусе мышц, то на ту сторону, где мышцы более зажаты, сделайте на 1 подход больше.',
        null, 3),
       ('Исходное положение может быть лежа на полу, сидя с опорой за спиной или стоя с опорой за спиной.', null, 4),
       ('Направляйте 4 шейный позвонок к опоре (либо полу), затылком отодвигайтесь от плечей, подбородок направляйте по направлени. к ушам.',
        null, 4),
       ('Лягте на спину (подготовившись к шавасане), пододвиньте стул стул так, чтобы лицо оказалось по сидением.',
        null, 5),
       ('На сидение стула повесьте ремень, застегнутый в петлю,либо  палантин (не тянущийся). Положите голову на петлю. Длина петли должна быть такая, чтобы когда голова лежит на петле, шея была в нейтральном положении (без сгибания), шея - продолжение вытягивающегося позвоночника.',
        null, 5),
       ('Часть петли, которая лежит на сиденье стула можно слегка отодвинуть от головы, чтобы появилось ощущение, что петля оттягивает затылок от плечей. Выполните шавасану 15 минут.',
        null, 5);
